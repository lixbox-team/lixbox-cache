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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Cette classe assure le démarrage de l'application JAX-RS
 * du Service Registry.
 * 
 * @author ludovic.terral
 */
@ApplicationPath("/cache/api")
public class CacheServiceApp extends Application 
{
    // ----------- Attribut(s) -----------
    private Set<Object> singletons = new HashSet<>();

    
    
    // ----------- Methode(s) -----------
    @Override
    public Set<Object> getSingletons() 
    {
        return singletons;
    }
}