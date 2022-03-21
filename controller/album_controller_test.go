package controller

import "testing"

func TestLikedAlbum(t *testing.T) {
	result := LikedAlbum("1", "7c20f5ef71c38abb1d0c1f1b6eb4459f")
	if result != nil {
		t.Error("Like Album Gagal : " + result.Error())
	}
}
