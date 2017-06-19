package com.example.benjamin.statusbardemo.common;

import java.util.Random;

/**
 * Created by Benjamin on 2016/12/5.
 */

public class TestImageUriApi {
    public static String[] imageUri = new String[]{
            "http://img.hb.aicdn.com/d18ea04280474c33b8df8ecde77c1bc6ca069d902a1e1-nAmFiG_fw658",
            "http://img.hb.aicdn.com/8e36d782b9b4d3eba0fa46dfaeb37f776d6aa10256b43-Dz9e0J_fw658",
            "http://img.hb.aicdn.com/4fa6a7e1d17c612f6fa506e0c7e7998ba8194df815e36-7cit8O_fw658",
            "http://img.hb.aicdn.com/ddb944db54df57bb1bfce5e8637cdb5ed71719aba80ce-MHg64I_fw658",
            "http://img.hb.aicdn.com/21d20f0a4f75ed2cb07afd519bb356000457bea1a9e6b-ztQUdE_fw658",
            "http://img.hb.aicdn.com/812117d7c8779da7635b34c311d35c964f2b82685ad7e-wUd9uE_fw658",
            "http://img.hb.aicdn.com/4911c461470534d8af1dcc9b214fc82b2061967952500-5epXuL_fw658",
            "http://img.hb.aicdn.com/1d394e7ac3b858f9bedacb0bb7f40f76ccc8a80136aae-tqQMC7_fw658",
            "http://img.hb.aicdn.com/ae5c3e95d1ebfefb4d39f0f6f20bdc99707bf59e7ae03-viuCI4_fw658",
            "http://img.hb.aicdn.com/4ff4f08806873bdf630313e62b4251123791836d1f152-tpFdah_fw658",
            "http://img.hb.aicdn.com/f8c9a07fbac1cd3ef4af1abf068c67bb832465492e66f-FSzN2J_fw658",
            "http://img.hb.aicdn.com/4808c67680c64dd837b93619d1ad20a16ed5e9426c1d7-WU0CPV_fw658",
            "http://img.hb.aicdn.com/0ea17f08c905451c70d43662c4d53751c612612541d31-dKjKNR_fw658",
            "http://img.hb.aicdn.com/31d3d3e9ed2485610bb169660d53306ab5552c8126979-EhTBxA_fw658",
            "http://img.hb.aicdn.com/17a9932092439e99c036c2351b0e703cd9eeb58923c2e-UrHBpJ_fw658",
            "http://img.hb.aicdn.com/2367fac79bf15fd10b7c2a6eeca4f46979e895432366f-c7sKJu_fw658",
            "http://img.hb.aicdn.com/4cceabc3fa57f3cb24615fefe6ac77c1b51e1265b7c0-kRYLtN_fw658",
            "http://img.hb.aicdn.com/5b0bc0b54bce1967b72e2e2c33ae782aca4338ef106d2-UxstFi_fw658",
            "http://img.hb.aicdn.com/2f65b1c7d55f1298666b3a0f145acbe914809fc22cc78-Tm9IVd_fw658",
            "http://img.hb.aicdn.com/e415684c75c00ec51106ae71aa9ca452c6ce0e4b413a0-nDuHrd_fw658",
            "http://img.hb.aicdn.com/9eedc4a53de650799bfcd4d8a22ba359c44a10b53c7c8-EaRuj7_fw658"
    };

    public static String getOneImageUri() {
        Random random = new Random();
        return imageUri[random.nextInt(imageUri.length)];
    }
}
