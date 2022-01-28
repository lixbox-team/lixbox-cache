/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 * This file is part of lixbox-service registry.
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
 *    along with lixbox-service registry.  If not, see <https://www.gnu.org/licenses/>
 *   
 *   @AUTHOR Lixbox-team
 *
 ******************************************************************************/
package fr.lixbox.service.cache.ui.router;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.lixbox.common.stream.util.StreamStringUtil;
import fr.lixbox.io.json.JsonUtil;
import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

/**
 * Ce roueur assure le dispatch des différentes vues.
 * 
 * @author ludovic.terral
 */
@ApplicationScoped 
public class Routes implements Serializable
{
    // ----------- Attributs -----------    
    private static final long serialVersionUID = 202201281909L;
    private static final Log LOG = LogFactory.getLog(Routes.class);
    
    @ConfigProperty(name="cache.api.uri") String cacheApi;
    

    // ----------- Methodes -----------
    @Route(path = "/configuration")
    void routeToConfiguration(RoutingContext rc)
    {
        String response = "";
        Map<String,String> config = new HashMap<>();
        config.put("cache.api.uri", cacheApi);
        response = JsonUtil.transformObjectToJson(config,false);
        
        rc.response()
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
            .end(response, asr->{
                if(asr.succeeded()) 
                {
                    LOG.debug("success....");
                } 
                else 
                {
                    LOG.error("Something went wrong " + asr.cause());
                }
            });        
    }
    
    
    
    @Route(path = "/cache")
    void routeToRegistry(RoutingContext rc)
    {
        String response = StreamStringUtil.read(Routes.class.getResourceAsStream("/META-INF/resources/index.html"));
        rc.response()
            .putHeader(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
            .end(response, asr->{
                if(asr.succeeded()) 
                {
                    LOG.debug("success....");
                } 
                else 
                {
                    LOG.error("Something went wrong " + asr.cause());
                }
            });        
    }
}
