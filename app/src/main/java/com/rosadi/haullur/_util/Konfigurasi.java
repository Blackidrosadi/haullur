package com.rosadi.haullur._util;

public class Konfigurasi {
    /**
     * URL FILE PHP
     */
    public static final String URL  = "http://192.168.1.34:8080/haullur/";

    public static final String URL_LOGIN    = URL + "user/login.php";

    public static final String URL_LOAD_KELUARGA    = URL + "keluarga/load.php";
    public static final String URL_CARI_KELUARGA    = URL + "keluarga/cari.php?cari=";
    public static final String URL_LOAD_KELUARGA_TERBARU    = URL + "keluarga/loadTerbaru.php";

    public static final String URL_TAMBAH_KELUARGA    = URL + "keluarga/tambah.php";
    public static final String URL_EDIT_KELUARGA    = URL + "keluarga/edit.php";
    public static final String URL_HAPUS_KELUARGA    = URL + "keluarga/hapus.php";

    public static final String URL_LOAD_ALMARHUM    = URL + "keluarga/loadAlmarhum.php?id=";
    public static final String URL_TAMBAH_ALMARHUM    = URL + "keluarga/tambahAlmarhum.php";
    public static final String URL_EDIT_ALMARHUM    = URL + "keluarga/editAlmarhum.php";
    public static final String URL_HAPUS_ALMARHUM    = URL + "keluarga/hapusAlmarhum.php";

    /**
     * TAG / KEY
     */
    public static final String KEY_USER_PREFERENCE = "user";
    public static final String KEY_USER_ID_PREFERENCE = "id_pref";
    public static final String KEY_USER_NAMA_PREFERENCE = "nama_pref";
    public static final String KEY_USER_EMAIL_PREFERENCE = "email_pref";
    public static final String KEY_USER_TELEPON_PREFERENCE = "telepon_pref";
    public static final String KEY_USER_SANDI_PREFERENCE = "sandi_pref";
    public static final String KEY_USER_LEVEL_PREFERENCE = "level_pref";
    public static final String KEY_JSON_ARRAY_RESULT = "result";

    public static final String KEY_ID = "id";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TELEPON = "telepon";
    public static final String KEY_SANDI = "sandi";
    public static final String KEY_LEVEL = "level";

    public static final String KEY_RT = "rt";

    public static final String KEY_ID_KELUARGA = "id";
}
