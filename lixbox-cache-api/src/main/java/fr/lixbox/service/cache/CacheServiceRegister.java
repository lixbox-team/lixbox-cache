/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 *   Copyrigth - LIXTEC - Tous droits reserves.
 *   
 *   Le contenu de ce fichier est la propriete de la societe Lixtec.
 *   
 *   Toute utilisation de ce fichier et des informations, sous n'importe quelle
 *   forme necessite un accord ecrit explicite des auteurs
 *   
 *   @AUTHOR Ludovic TERRAL
 *
 ******************************************************************************/
package fr.lixbox.service.cache;

import java.net.InetAddress;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.lixbox.service.registry.cdi.LocalRegistryConfig;
import fr.lixbox.service.registry.client.RegistryServiceClient;
import fr.lixbox.service.registry.model.ServiceType;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

/**
 * Cette classe enregistre le service de cache dans le registry service.
 * 
 * @author ludovic.terral
 */
@Singleton
public class CacheServiceRegister 
{
    // ----------- Attribut(s) -----------  
    private static final Log LOG = LogFactory.getLog(CacheServiceRegister.class);
    private static final String UNABLE_TO_REGISTER_TXT = "UNABLE TO REGISTER "; 
    
    @Inject @LocalRegistryConfig RegistryServiceClient registryClient;
    @ConfigProperty(name="quarkus.http.port") int hostPort;
    private String endpointURI;

    

    // ----------- Methode(s) -----------
    public void registerService(@Observes StartupEvent ev)
    {
        try
        {
            InetAddress inetAddress = InetAddress.getLocalHost();
            endpointURI = "http://" + inetAddress.getHostAddress()+ ":" + hostPort + CacheService.FULL_SERVICE_URI;
            boolean result = registryClient.registerService(CacheService.SERVICE_NAME, CacheService.SERVICE_VERSION, ServiceType.MICRO_PROFILE, endpointURI);
            LOG.info("SERVICE CACHE REGISTRATION IS "+result+" ON "+registryClient.getCurrentRegistryServiceUri());
        }
        catch(NullPointerException e)
        {
            LOG.info(UNABLE_TO_REGISTER_TXT+CacheService.SERVICE_NAME+"-"+CacheService.SERVICE_VERSION+": absence d'annuaire");
        }
        catch(Exception e)
        {
            LOG.error(UNABLE_TO_REGISTER_TXT+CacheService.SERVICE_NAME+"-"+CacheService.SERVICE_VERSION+": "+ExceptionUtils.getRootCauseMessage(e));
            LOG.error(e);
        }
    }
    
    
    
    public void unregisterService(@Observes ShutdownEvent ev)
    {
        try
        {
            boolean result = registryClient.unregisterService(CacheService.SERVICE_NAME, CacheService.SERVICE_VERSION, endpointURI);
            LOG.info("SERVICE CACHE UNREGISTRATION IS "+result+" ON "+registryClient.getCurrentRegistryServiceUri());
        }
        catch(Exception e)
        {
            LOG.error(UNABLE_TO_REGISTER_TXT+CacheService.SERVICE_NAME+"-"+CacheService.SERVICE_VERSION+": "+ExceptionUtils.getRootCauseMessage(e));
            LOG.trace(e);
        }
    }
}