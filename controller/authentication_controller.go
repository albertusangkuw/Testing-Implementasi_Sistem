package controller

import (
	"math/rand"

	"github.com/dgrijalva/jwt-go"
)

type Claims struct {
	ID       string `json:"id"`
	Name     string `json:"name"`
	UserType int    `json:"usertype"`
	jwt.StandardClaims
}

func RandStringBytesRmndr(n int) string {
	const letterBytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	b := make([]byte, n)
	for i := range b {
		b[i] = letterBytes[rand.Int63()%int64(len(letterBytes))]
	}
	return string(b)
}
