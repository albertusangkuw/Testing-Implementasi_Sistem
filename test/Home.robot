*** Settings ***
Documentation  Scenario Testing Home
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
*** Test Cases ***
Melihat Menu Home dengan informasi sudah login
    [documentation]  Test case ini untuk memastikan bahwa dalam proses untuk mendapatkan informasi pada 
    ...  Menu Home seperti history dan data detail dapat didapatkan dengan syarat login terlebih dahulu
    [tags]  Home
    Create Session  mysession  ${URL_API}  
    ${response}=  GET On Session  mysession  /login  params=email=${LOGIN_EMAIL}&password=${LOGIN_PASSWORD}
    Status Should Be  200  ${response}  #Check Status as 200
   
    # Mendapatkan informasi umum User dari endpoint “/users/{userID}”
    ${response}=  GET On Session  mysession  /users/${userID}
    Status Should Be  200  ${response}  #Check Status as 200

    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200'
    
    # Mendapatkan foto profile dari endpoint “/users/{userID}/photo”
    ${response}=  GET On Session  mysession  /users/${userID}/photo
    Status Should Be  200  ${response}  #Check Status as 200

    # Mendapatkan daftar lagu yang terakhir didengarkan dari endpoint “/users/{userID}/history”
    ${response}=  GET On Session  mysession  /users/${userID}/history
    Status Should Be  200  ${response}  #Check Status as 200

    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200'

    # Mendapatkan informasi pengikut, informasi diikuti, data playlist melalui endpoint “/users/{userID}/detail”
    ${response}=  GET On Session  mysession  /users/${userID}/detail
    Status Should Be  200  ${response}  #Check Status as 200

    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200'
    