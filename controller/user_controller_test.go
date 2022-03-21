package controller

import (
	"errors"
	"testing"
)

func TestResetPassword(t *testing.T) {
	tableTest := []struct {
		label    string
		email    string
		expected struct {
			id   string
			name string
			err  error
		}
	}{
		{
			label: "Reset Password with Known Email",
			email: "neilea@gmail.com",
			expected: struct {
				id   string
				name string
				err  error
			}{
				id:   "9f44c64241f6095571dea1b73144a5fb",
				name: "neilea",
				err:  nil,
			},
		},
		{
			label: "Reset Password with Unknown Email",
			email: "uknown@mail.com",
			expected: struct {
				id   string
				name string
				err  error
			}{
				id:   "",
				name: "",
				err:  errors.New("404"),
			},
		},
	}
	for _, v := range tableTest {
		t.Run(
			v.label,
			func(t *testing.T) {
				id, name, err := ResetPassword(v.email)
				if v.expected.id != id {
					t.Error("Expected:", v.expected.id, ",got:", id)
				}
				if v.expected.name != name {
					t.Error("Expected:", v.expected.name, ",got:", name)
				}
				if err != nil && v.expected.err != nil && errors.Is(v.expected.err, err) {
					t.Error("Expected:", v.expected.err, ",got:", err)
				}
			},
		)
	}
}

func TestDeleteUser(t *testing.T) {
	userID := "a39df23e422a97f01a3b0ba58d7c9d8e"
	result := DeleteUser(userID)
	if result != nil {
		t.Error("Test Delete User Gagal : " + result.Error())
	}
}

func TestLoginRegularUser(t *testing.T) {
	tableTest := []struct {
		label    string
		email    string
		password string
		expected struct {
			id   string
			name string
			err  error
		}
	}{
		{
			label:    "Login with Known Correct Credentials",
			email:    "albertusaaa@gmail.com",
			password: "12345678",
			expected: struct {
				id   string
				name string
				err  error
			}{
				id:   "7c20f5ef71c38abb1d0c1f1b6eb4459f",
				name: "albertusaa222",
				err:  nil,
			},
		},
		{
			label:    "Login with Wrong Credentials",
			email:    "albertusaaa@gmail.com",
			password: "abcdefg",
			expected: struct {
				id   string
				name string
				err  error
			}{
				id:   "",
				name: "",
				err:  errors.New("404"),
			},
		},
	}
	for _, v := range tableTest {
		t.Run(
			v.label,
			func(t *testing.T) {
				id, name, err := UserLogin(v.email, v.password)
				if v.expected.id != id {
					t.Error("Expected:", v.expected.id, ",got:", id)
				}
				if v.expected.name != name {
					t.Error("Expected:", v.expected.name, ",got:", name)
				}
				if err != nil && v.expected.err != nil && errors.Is(v.expected.err, err) {
					t.Error("Expected:", v.expected.err, ",got:", err)
				}
			},
		)
	}
}
