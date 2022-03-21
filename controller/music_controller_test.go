package controller

import "testing"

func TestLikeSong(t *testing.T) {
	idMusic := "1"
	idUser := "7c20f5ef71c38abb1d0c1f1b6eb4459f"
	result := LikeSong(idMusic, idUser)
	if result != nil {
		t.Error("Like Song Gagal : " + result.Error())
	}
}
