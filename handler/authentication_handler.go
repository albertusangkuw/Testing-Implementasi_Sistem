package handler

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	controller "github.com/Testing-Implementasi_Sistem/controller"
	model "github.com/Testing-Implementasi_Sistem/model"
	"github.com/dgrijalva/jwt-go"
)

var jwtKey = []byte(controller.RandStringBytesRmndr(128))
var tokenName = "token"

func GenerateToken(w http.ResponseWriter, id string, name string, userType int) {
	// Expire in 1 day
	tokenExpiryTime := time.Now().Add(60 * 24 * time.Minute)

	claims := &controller.Claims{
		ID:       id,
		Name:     name,
		UserType: userType,
		StandardClaims: jwt.StandardClaims{
			ExpiresAt: tokenExpiryTime.Unix(),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	signedToken, err := token.SignedString(jwtKey)
	if err != nil {
		return
	}

	http.SetCookie(w, &http.Cookie{
		Name:     tokenName,
		Value:    signedToken,
		Expires:  tokenExpiryTime,
		Secure:   false,
		HttpOnly: true,
	})

}

func resetUserToken(w http.ResponseWriter) {
	http.SetCookie(w, &http.Cookie{
		Name:     tokenName,
		Value:    "",
		Expires:  time.Now(),
		Secure:   false,
		HttpOnly: true,
	})
}

func Authenticate(next http.HandlerFunc, accessType int) http.HandlerFunc {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		isValidToken := validateUserToken(w, r, accessType)
		if !isValidToken {
			var response model.UserResponse
			response.Status = 401
			response.Message = "Unauthorized Access"
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(response)
		} else {
			next.ServeHTTP(w, r)
		}
	})
}

func validateUserToken(w http.ResponseWriter, r *http.Request, accessType int) bool {
	isAccessTokenValid, id, _, userType := validateUserTokenFromCookies(r)
	if isAccessTokenValid {
		isUserValid := userType == accessType
		if isUserValid {
			fmt.Print("ID User trigger : ", id)
			return true
		}
	}
	return false
}

func validateUserTokenFromCookies(r *http.Request) (bool, string, string, int) {
	if cookie, err := r.Cookie(tokenName); err == nil {
		accessToken := cookie.Value
		accessClaims := &controller.Claims{}
		parsedToken, err := jwt.ParseWithClaims(accessToken, accessClaims, func(accessToken *jwt.Token) (interface{}, error) {
			return jwtKey, nil
		})

		if err == nil && parsedToken.Valid {
			return true, accessClaims.ID, accessClaims.Name, accessClaims.UserType
		}
	}
	return false, "", "", -1
}
