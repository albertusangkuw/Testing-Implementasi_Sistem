*** Settings ***
Documentation  Scenario Testing Registrasi
Library  SeleniumLibrary
Library  RequestsLibrary
Library  JSONLibrary
Library  Collections
Library  String

*** Variables ***
${URL_API}  http://localhost:8081 

*** Test Cases ***
Melakukan Dengan data lengkap
    [documentation]  Test case ini untuk memastikan bahwa dalam melakukan registrasi bisa dilakukan
    ...  dengan data yang lengkap
    [tags]  Registrasi
    Create Session  mysession  ${URL_API}  
    ${RANUSER}  Generate Random String  11  [LETTERS]
    
    &{body}=  Create Dictionary  username=${RANUSER}  email=albertusaaa@gmail.com  password=12345678  country=Indonesia  dateJoin=2021-10-11  categories=1  urlphotoprofile=domain.com/img.jpg
    &{header}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded

    ${response}=  POST On Session  mysession  /registrasi  headers=${header}  data=${body} 
    Status Should Be  200  ${response}  
    
    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '200'
    
Melakukan Dengan data tidak lengkap
    [documentation]   Test case ini untuk memastikan bahwa dalam melakukan registrasi tidak bisa dilakukan
    ...  dengan data yang tidak lengkap
    [tags]  Registrasi
    Create Session  mysession  ${URL_API}  
    ${RANUSER}  Generate Random String  11  [LETTERS]
    
    &{body}=  Create Dictionary  username=${RANUSER}  email=albertusaaa@gmail.com  password=12345678   urlphotoprofile=domain.com/img.jpg
    &{header}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded

    ${response}=  POST On Session  mysession  /registrasi  headers=${header}  data=${body} 
    
    ${status}=  Get Value From Json  ${response.json()}  status  
    ${intStatus}=  Get From List   ${status}  0
    Should be equal  '${intStatus}'  '400'

*** Keywords ***