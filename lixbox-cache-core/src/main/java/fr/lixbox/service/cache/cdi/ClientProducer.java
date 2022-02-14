/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 *    This file is part of lixbox-service.
 *
 *    lixbox-cache is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    lixbox-cache is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *    along with lixbox-cache.  If not, see <https://www.gnu.org/licenses/>
 *   
 *   @AUTHOR Lixbox-team
 *
 ******************************************************************************/
package fr.lixbox.service.cache.cdi;

import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.lixbox.service.registry.cdi.LocalRegistryConfig;
import fr.lixbox.service.registry.client.RegistryServiceClient;

/**
 * Cette classe assure la production des clients pour le CDI
 * 
 * @author Ludovic.terral
 */
public class ClientProducer
{
    // ----------- Attribut(s) -----------  
    @ConfigProperty(name="registry.uri") String registryUri;

    

    // ----------- Methode(s) -----------
    @Produces @LocalRegistryConfig
    public RegistryServiceClient getServiceRegistryClient() 
    {
        RegistryServiceClient result = null;
        try
        {
            result = new RegistryServiceClient(registryUri);
        }
        catch(Exception e)
        {
            //absence de service d'annuaire.
        }
        return result;
    }
}
