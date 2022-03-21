package controller

import (
	"testing"

	"github.com/Testing-Implementasi_Sistem/model"
)

func TestResponseManager(t *testing.T) {
	tableTest := []struct {
		label           string
		responseManager model.Response
		status          int16
		message         string
		expected        model.Response
	}{
		{
			label:           "Response Ok",
			responseManager: model.Response{},
			status:          200,
			message:         "Success",
			expected:        model.Response{Status: 200, Message: "OK - Success"},
		},
		{
			label:           "Response Not Found",
			responseManager: model.Response{},
			status:          404,
			message:         "In Database",
			expected:        model.Response{Status: 404, Message: "Not Found - In Database"},
		},
		{
			label:           "Response Internal Error",
			responseManager: model.Response{},
			status:          500,
			message:         "MySQL Query failed at line 20",
			expected:        model.Response{Status: 500, Message: "Internal Server Error - MySQL Query failed at line 20"},
		},
		{
			label:           "Response  Not Available",
			responseManager: model.Response{},
			status:          700,
			message:         "Crash were found between Services",
			expected:        model.Response{Status: 501, Message: "Not Implemented - Crash were found between Services"},
		},
	}
	for _, v := range tableTest {
		t.Run(
			v.label,
			func(t *testing.T) {
				ResponseManager(&v.responseManager, v.status, v.message)
				if v.expected != v.responseManager {
					t.Error("Expected:", v.expected, ",got:", v.responseManager)
				}
			},
		)
	}
}

func TestMD5Hash(t *testing.T) {
	tableTest := []struct {
		label    string
		text     string
		expected string
	}{
		{label: "MD5 Origin", text: "supersecret", expected: "9a618248b64db62d15b300a07b00580b"},
		{label: "MD5 with Small Origin Diff.", text: "Supersecret", expected: "6a582edbeb20c95978af4ebec2cedddc"},
	}
	for _, v := range tableTest {
		t.Run(
			v.label,
			func(t *testing.T) {
				result := GetMD5Hash(v.text)
				if v.expected != result {
					t.Error("Expected:", v.expected, ",got:", result)
				}
			},
		)
	}
}
