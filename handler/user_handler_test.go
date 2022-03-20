package handler

import (
	"encoding/json"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"net/url"
	"strconv"
	"strings"
	"testing"
	"time"
)

func TestHandlerLoginRegularUser(t *testing.T) {
	tableTest := []struct {
		label    string
		email    string
		password string
		expected int
	}{
		{label: "Login with Correct Credential", email: "albertusaaa@gmail.com", password: "12345678", expected: 200},
		{label: "Login with Wrong Password", email: "dahdkj@gmail", password: "notpwd", expected: 404},
		{label: "Login with Unknown User", email: "testingtest@gmail", password: "notexistuser", expected: 404},
	}
	for _, v := range tableTest {
		t.Run(
			v.label,
			func(t *testing.T) {
				req, err := http.NewRequest("GET", "localhost:8081/login?email="+v.email+"&password="+v.password, nil)
				if err != nil {
					t.Fatalf("Could not create request: %v", err)
				}
				rec := httptest.NewRecorder()
				UserLogin(rec, req)

				res := rec.Result()
				defer res.Body.Close()

				b, err := ioutil.ReadAll(res.Body)
				if err != nil {
					t.Fatalf("Could not read response: %v", err)
				}
				if res.StatusCode != http.StatusOK {
					t.Fatalf("Expected status OK; got %v", res.Status)
				}
				var bodyMapJson map[string]interface{}
				json.Unmarshal(b, &bodyMapJson)

				if v.expected != int(bodyMapJson["status"].(float64)) {
					t.Error("Expected:", v.expected, ",got:", bodyMapJson["status"])
				}
			},
		)
	}
}

func TestHandlerRegistrasiRegularUser(t *testing.T) {
	tableTest := []struct {
		label           string
		username        string
		email           string
		password        string
		country         string
		urlphotoprofile string
		dateJoin        string
		expected        int
	}{
		{
			label:           "Registrasi Login with Correct Form",
			username:        "testRegister1",
			email:           "testReg1@gmail.com",
			password:        "12345678",
			country:         "Indonesia",
			urlphotoprofile: "https://upload.wikimedia.org/wikipedia/en/9/95/Test_image.jpg",
			dateJoin:        time.Now().Format("2006-01-02"),
			expected:        200,
		},
		{
			label:           "Registrasi Login without Password Form",
			username:        "testRegister2",
			email:           "testReg2@gmail.com",
			password:        "",
			country:         "",
			urlphotoprofile: "https://upload.wikimedia.org/wikipedia/en/9/95/Test_image.jpg",
			dateJoin:        time.Now().Format("2006-01-02"),
			expected:        400,
		},
		{
			label:           "Registrasi Login without Email Form",
			username:        "testRegister3",
			email:           "",
			password:        "12345678",
			country:         "Indonesia",
			urlphotoprofile: "https://upload.wikimedia.org/wikipedia/en/9/95/Test_image.jpg",
			dateJoin:        time.Now().Format("2006-01-02"),
			expected:        400,
		},
	}
	for _, v := range tableTest {
		t.Run(
			v.label,
			func(t *testing.T) {
				data := url.Values{}
				data.Set("username", v.username)
				data.Set("email", v.email)
				data.Set("password", v.password)
				data.Set("country", v.country)
				data.Set("urlphotoprofile", v.urlphotoprofile)
				data.Set("dateJoin", v.dateJoin)

				req, err := http.NewRequest("POST", "localhost:8081/registrasi", strings.NewReader(data.Encode()))
				if err != nil {
					t.Fatalf("Could not create request: %v", err)
				}
				req.Header.Add("Content-Type", "application/x-www-form-urlencoded")
				req.Header.Add("Content-Length", strconv.Itoa(len(data.Encode())))

				rec := httptest.NewRecorder()
				RegisterRegularUser(rec, req)

				res := rec.Result()
				defer res.Body.Close()

				b, err := ioutil.ReadAll(res.Body)
				if err != nil {
					t.Fatalf("Could not read response: %v", err)
				}
				if res.StatusCode != http.StatusOK {
					t.Fatalf("Expected status OK; got %v", res.Status)
				}
				var bodyMapJson map[string]interface{}
				json.Unmarshal(b, &bodyMapJson)

				if v.expected != int(bodyMapJson["status"].(float64)) {
					t.Error("Expected:", v.expected, ",got:", bodyMapJson["status"], "-", bodyMapJson)
				}
			},
		)
	}
}
