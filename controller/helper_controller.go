package controller

import (
	"crypto/md5"
	"crypto/sha256"
	"encoding/hex"
	"net/http"

	model "github.com/Testing-Implementasi_Sistem/model"
)

func GenerateSQLWhere(r *http.Request, listColumn []string, logic string, requestParams string) (string, []interface{}) {
	// Using Variadic Function to pass
	filterBY := ""
	var valuesList []interface{}
	err := r.ParseForm()
	if err != nil {
		return filterBY, valuesList
	}
	for _, s := range listColumn {
		var data string
		if requestParams == "GET" {
			testData := r.URL.Query()[s]
			if testData == nil {
				continue
			}
			data = testData[0]

		} else if requestParams == "POST" {
			if s == "password" {
				sha := sha256.New()
				sha.Write([]byte(r.Form.Get(s)))
				data = hex.EncodeToString(sha.Sum(nil))
			} else {
				data = r.Form.Get(s)
			}

		}
		if len(data) > 0 {
			if filterBY != "" {
				filterBY += " " + logic + " "
			}
			filterBY += s + "=?"
			valuesList = append(valuesList, data)
		}
	}
	return filterBY, valuesList
}

func ResponseManager(response *model.Response, status int16, message string) {
	if message != "" {
		message = " - " + message
	}
	switch {
	case status == 200:
		response.Status = 200
		response.Message = "OK"
	case status == 400:
		response.Status = 400
		response.Message = "Bad Request"
	case status == 404:
		response.Status = 404
		response.Message = "Not Found"
	case status == 500:
		response.Status = 500
		response.Message = "Internal Server Error"
	default:
		response.Status = 501
		response.Message = "Not Implemented"
	}
	response.Message += message

}

func GetMD5Hash(text string) string {
	hash := md5.Sum([]byte(text))
	return hex.EncodeToString(hash[:])
}
