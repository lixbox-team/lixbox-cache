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
package fr.lixbox.service.registry;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Cette classe sert à tester la version déployée sur un serveur distant.
 * 
 * @author ludovic.terral
 *
 */
@QuarkusTest
public class CacheServiceApiTest
{    
    // ----------- Attribut(s) -----------   
    
    

    // ----------- Methode(s) -----------
    @Test
    public final void test_checkHealth()
    {        
        given()
          .when().get("/cache/api/1.0/health")
          .then()
            .statusCode(200);
    }
}