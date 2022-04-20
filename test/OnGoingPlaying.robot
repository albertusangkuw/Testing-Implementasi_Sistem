*** Settings ***
Documentation  Scenario Testing On Going Playing
Library  SeleniumLibrary
Library  RequestsLibrary
Library  JSONLibrary
Library  Collections
Library  String

*** Variables ***
${URL_API}  http://localhost:8081 
${LOGIN_EMAIL}  albertusaaa@gmail.com
${LOGIN_PASSWORD}  12345678  
${userID}  7d75b89967463de95fe37c55686a63e0
${IDmusic}  2
${IDalbum}  1
*** Test Cases ***
Melihat Menu On Going Playing dengan informasi sudah login
    [documentation]  Test case ini untuk memastikan bahwa dapat dilakukan pengambilan data music berdasarkan
    ...  IDMusic dan dilakukan pencatatan lagu dengan syarat sudah terdapat pengguna yang login
    [tags]  Smoke
    Create Session  mysession  ${URL_API}  
    ${response}=  GET On Session  mysession  /login  params=email=${LOGIN_EMAIL}&password=${LOGIN_PASSWORD}
    Status Should Be  200  ${response}  #Check Status as 200

    # Mendapatkan informasi umum lagu dari endpoint “/music/{IDmusic}”
    ${response}=  GET On Session  mysession  /music/${IDmusic}
    Status Should Be  200  ${response}  #Check Status as 200

    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200'    

    # Mendapatkan cover foto lagu dari endpoint “/album/{IDAlbum}/photo”
    ${response}=  GET On Session  mysession  /album/${IDAlbum}/photo
    Status Should Be  200  ${response}  #Check Status as 200

    # Mendapatkan data file lagu/music dari endpoint “/music/{IDmusic}/data”
    ${response}=  GET On Session  mysession  /music/${IDmusic}/data
    Status Should Be  200  ${response}  #Check Status as 200

    # Mencatat lagu yang didengarkan dari endpoint “/users/{userID}/history”
    ${response}=  GET On Session  mysession  /users/${userID}/history
    Status Should Be  200  ${response}  #Check Status as 200

    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200' 

