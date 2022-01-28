/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 * This file is part of lixbox-service cache.
 *
 *    lixbox-iam is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    lixbox-iam is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *    along with lixbox-service cache.  If not, see <https://www.gnu.org/licenses/>
 *   
 *   @AUTHOR Lixbox-team
 *
 ******************************************************************************/
package fr.lixbox.service.cache.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.util.CodeVersionUtil;
import fr.lixbox.common.util.StringUtil;
import fr.lixbox.service.cache.CacheService;
import fr.lixbox.service.registry.RegistryService;
import fr.lixbox.service.registry.model.health.Check;
import fr.lixbox.service.registry.model.health.ServiceState;
import fr.lixbox.service.registry.model.health.ServiceStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

/**
 * Ce service de cache fonctionne sur Redis.
 * 
 * @author ludovic.terral
 */
@ApplicationScoped
@Path(RegistryService.SERVICE_URI)
@Produces({"application/json"})
@Consumes({"application/json"})
public class RedisCacheServiceBean implements CacheService
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = -5032267261483670109L;
    private static final Log LOG = LogFactory.getLog(CacheService.class);
    
    private static final String SERVICE_REDIS_TEXT = "LE SERVICE REDIS ";
    
    @ConfigProperty(name="registry.uri") String registryUri;
    @ConfigProperty(name="cache.redis.uri") String redisUri;

    
    
    // ----------- Methode(s) -----------
    public RedisCacheServiceBean()
    {
        //a voir
    }



    @Override
    public ServiceState checkHealth() 
    {
        return checkReady();
    }

    
    
    @Override
    public ServiceState checkReady()
    {
        LOG.info("registry uri: "+registryUri);
        LOG.info("redis uri: "+redisUri);
        LOG.debug("Check Health started");
        ServiceState state = new ServiceState();
        
        //controle de redis
        if (!StringUtil.isEmpty(redisUri))
        {
            try (JedisPooled redisClient = new JedisPooled(redisUri))
            {           
                redisClient.keys("*");
                state.setStatus(ServiceStatus.UP);
                LOG.debug(SERVICE_REDIS_TEXT+redisUri+" EST DISPONIBLE");
            }
            catch (Exception e)
            {
                LOG.fatal(e,e);
                LOG.error(SERVICE_REDIS_TEXT+redisUri+" N'EST PAS DISPONIBLE");
                state.setStatus(ServiceStatus.DOWN);
                state.getChecks().add(new Check(ServiceStatus.DOWN, SERVICE_REDIS_TEXT+redisUri+" N'EST PAS DISPONIBLE"));
            }
        }
        else
        {
            state.setStatus(ServiceStatus.DOWN);
            state.getChecks().add(new Check(ServiceStatus.DOWN, "IMPOSSIBLE DE TROUVER LE SERVICE REDIS"));
        }
        LOG.debug("Check Health finished");
        if (state.getStatus().equals(ServiceStatus.DOWN))
        {
            throw new ProcessusException(state.toString());
        }
        return state;
    }
    
    
    
    @Override public ServiceState checkLive() 
    {
        return new ServiceState(ServiceStatus.UP);
    }



    /**
     * Cette methode renvoie la version courante du code. 
     */
    @Override
    public String getVersion()
    {   
        return CodeVersionUtil.getVersion(this.getClass());
    }
    
    

    /**
     * Cette methode renvoie une liste de clé correspondant à une ou plusieurs patterns.
     * Si aucune pattern n'est transmise, le wildcard est utilisé.
     * @param pattern
     * 
     * @return la liste des clés correspondantes.
     */
    @Override
    public List<String> getKeys(String pattern)
    {
        List<String> result=new ArrayList<>();
        try(Jedis redisClient = new Jedis(redisUri))
        {
            String internamPattern = StringUtil.isEmpty(pattern)?"*":pattern;
            result  = new ArrayList<>(redisClient.keys(internamPattern));
            Collections.sort(result);
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
        
    }
    
    

    /**
     * Cette methode renvoie la valeur associée à une clé
     * @param key
     * 
     * @return null si pas de valeur.
     */
    @Override
    public String get(String key)
    {
        String result = "";
        try(Jedis redisClient = new Jedis(redisUri))
        {
            result = redisClient.get(key);
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    }

    

    /**
     * Cette methode supprime une clé et sa valeur dans le cache.
     * @param key
     * 
     * @return true si la suppression est effective.
     */
    @Override
    public boolean remove(String key)
    {
        boolean result=false;
        try(Jedis redisClient = new Jedis(redisUri))
        {
            if (redisClient.del(key)>0)
            {
                result = true;
            } 
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    }

    

    /**
     * Cette methode supprime les clés et leurs valeurs dans le cache.
     * @param keys
     * 
     * @return true si la suppression est effective.
     */
    @Override
    public boolean remove(String... keys)
    {
        boolean result=false;
        try(Jedis redisClient = new Jedis(redisUri))
        {
            if (keys!=null && redisClient.del(keys)>0)
            {
                result = true;
            } 
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    }
      
    
    
    /**
     * Cette methode renvoie le nombre de clés qui correspondent à une pattern.
     * Si la pattern n'est pas renseigné le wildcar est utilisé.
     * @param pattern
     * 
     * return le nombre de clés.
     */
    @Override    
    public int size(String pattern)
    {
        int result=0;
        try(Jedis redisClient = new Jedis(redisUri))
        {
            String internamPattern = StringUtil.isEmpty(pattern)?"*":pattern;
            List<String> temp  = new ArrayList<>(redisClient.keys(internamPattern));
            result = temp.size();
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    }
    
    

    /**
     * Cette methode verifie la présence d'une clé.     
     * @param pattern
     * 
     * return true si la clé est présente.
     */
    @Override
    public boolean containsKey(String pattern)
    {  
        boolean result;
        List<String> tmp  = getKeys(pattern);        
        result = !tmp.isEmpty();
        return result;
    }
        
    
    
    /**
     * Cette methode insère une clé et sa valeur dans le cache.
     * @param key
     * @param value
     * 
     * @return true si l'enregistrement est effectif.
     */
    @Override   
    public boolean put(String key, String value)
    {
        boolean result=false;
        try(Jedis redisClient = new Jedis(redisUri))
        {
            if (!StringUtil.isEmpty(key))
            {
                result = !StringUtil.isEmpty(redisClient.set(key,value));
            }
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    }
    

    
    /**
     * Cette methode efface l'ensemble des données du cache.
     * 
     * @return true si le nettoyage est ok.
     */
    @Override   
    public boolean clear()
    {
        boolean result=false;
        try(Jedis redisClient = new Jedis(redisUri))
        {
            redisClient.del(redisClient.keys("*").toArray(new String[0]));
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    }
    

    
    
    /**
     * Cette methode enregistre les associations clé valeur dans le cache.
     * 
     * @param values
     * 
     * @return true si l'écriture est ok
     */
    @Override   
    public boolean put(Map<String,String> values)
    {
        boolean result=false;
        try(Jedis redisClient = new Jedis(redisUri))
        {
            
            List<String> tmp = new ArrayList<>();        
            for (Entry<String, String> entry : values.entrySet())
            {
                tmp.add(entry.getKey());
                tmp.add(entry.getValue());
            }                
            result = redisClient.mset(tmp.toArray(new String[0])).contains("OK");
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    }

    
    
    /**
     * Cette methode récupère les valeurs associées à la liste des clés
     * fournie en paramètres
     * 
     * @param keys
     * 
     * @return la liste des valeurs
     */
    @Override   
    public Map<String, String> get(String... keys)
    {
        Map<String,String> result = new HashMap<>();
        try(Jedis redisClient = new Jedis(redisUri))
        {
            List<String> values = redisClient.mget(keys);                
            for (int ix=0; ix<keys.length; ix++)
            {
                result.put(keys[ix], values.get(ix));
            }
        }
        catch(Exception e)
        {
            LOG.fatal(ExceptionUtils.getRootCauseMessage(e),e);
        }
        return result;
    } 
}