package controller

import "testing"

func TestRegistrasiRegularUser(t *testing.T) {
	hasilErr := RegisterRegularUser("albetgf", "dahdkj@gmail", "12345", "India", "photo.jpg", "2021-10-11", 2)
	if hasilErr != nil {
		t.Error("Registrasi User Gagal :" + hasilErr.Error())
	}
}

func TestRegistrasiRegularUserNoPassword(t *testing.T) {
	hasilErr := RegisterRegularUser("NoPwdalbetawgf", "dahdkj@gmail", "", "India", "photo.jpg", "2021-10-11", 2)
	if (hasilErr != nil && hasilErr.Error() == "400") == false {
		t.Error("Registrasi User No Password Gagal :" + hasilErr.Error())
	}
}
