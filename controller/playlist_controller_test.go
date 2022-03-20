package controller

import "testing"

func TestAddSongPlaylist(t *testing.T) {
	result := AddSongPlaylist("111", "222")
	if result != nil {
		t.Error("Add Song to Playlist Gagal : " + result.Error())
	}
}

func TestFollowedPlaylist(t *testing.T) {
	result := FollowedPlaylist("111", "222")
	if result != nil {
		t.Error("Followed Playlist Gagal : " + result.Error())
	}
}

func TestUpdatePlaylist(t *testing.T) {
	result := FollowedPlaylist("111", "222")
	if result != nil {
		t.Error("Followed Playlist Gagal : " + result.Error())
	}
}
