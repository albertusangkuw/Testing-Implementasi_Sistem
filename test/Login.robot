*** Settings ***
Documentation  Scenario Testing Login
Library  SeleniumLibrary
Library  RequestsLibrary
Library  JSONLibrary
Library  Collections
Library  String

*** Variables ***
${URL_API}  http://localhost:8081 
${LOGIN_EMAIL}  albertusaaa@gmail.com
${LOGIN_PASSWORD}  12345678
*** Test Cases ***
Melakukan Login dengan Valid Kredensial
    [documentation]  Test case ini untuk memastikan bahwa dalam melakukan login dengan data pengguna benar yang 
    ...  sudah diregistrasi sebelumnya bisa melakukan login
    [tags]  Login
    Create Session  mysession  ${URL_API}  
    ${response}=  GET On Session  mysession  /login  params=email=${LOGIN_EMAIL}&password=${LOGIN_PASSWORD}
    Status Should Be  200  ${response}  #Check Status as 200

    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200'
    
Melakukan Login dengan Password Salah
    [documentation]  Test case ini untuk memastikan bahwa dalam melakukan login dengan data pengguna salah yang 
    ...  sudah diregistrasi sebelumnya tidak bisa melakukan login karena memberikan informasi password salah
    [tags]  Login
    Create Session  mysession  ${URL_API}  
    ${response}=  GET On Session  mysession  /login  params=email=${LOGIN_EMAIL}&password=wrongpwd
    
    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '404'

Melakukan Login dengan Pengguna Belum Terdaftar
    [documentation]   Test case ini untuk memastikan bahwa dalam melakukan login dengan data pengguna yang 
    ...  belum  diregistrasi sebelumnya maka tidak bisa melakukan login
    [tags]  Login
    Create Session  mysession  ${URL_API}  
    ${response}=  GET On Session  mysession  /login  params=email=noregister@test.com&password=12345678
    
    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '404'   

*** Keywords ***