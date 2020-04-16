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
    @ConfigProperty(name="registry.uri") private String registryUri;

    

    // ----------- Methode(s) -----------
    @Produces @LocalRegistryConfig
    public RegistryServiceClient getServiceRegistryClient() 
    {
        return new RegistryServiceClient(registryUri);
    }
}
