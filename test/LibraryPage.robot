*** Settings ***
Documentation  Scenario Testing Library Page
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
Melihat Menu Library dengan informasi sudah login
    [documentation]  Test case ini untuk memastikan bahwa dalam pengaksesan data Library 
    ...  bisa mendapatkan informasi yang dibutuhkan untuk menampilkan menu dengan syarat sudah login
    [tags]  MenuLibrary
    
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

    # Mendapatkan informasi pengikut, informasi diikuti, data playlist melalui endpoint “/users/{userID}/detail”
    ${response}=  GET On Session  mysession  /users/${userID}/detail
    Status Should Be  200  ${response}  #Check Status as 200

    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200'