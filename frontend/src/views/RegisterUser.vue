<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card>
          <v-toolbar color="deep-orange" dark flat>
            <v-toolbar-title>회원가입</v-toolbar-title>
            <v-spacer />
          </v-toolbar>
          <v-card-text>
            <v-form ref="form" v-model="valid" @submit.prevent="onSubmitForm">
              <v-text-field label="이메일" type="email" v-model="email" :rules="emailRules" required></v-text-field>
              <v-text-field
                label="비밀번호"
                :type="showPassword ? 'text' : 'password'"
                :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append="showPassword = !showPassword"
                v-model="password"
                :rules="passwordRules"
                @keyup="checkPassword"
                required
              />
              <v-text-field
                label="비밀번호확인"
                :type="showPasswordCheck ? 'text' : 'password'"
                :append-icon="showPasswordCheck ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append="showPasswordCheck = !showPasswordCheck"
                v-model="passwordCheck"
                :rules="passwordCheckRules"
                required
              />
              <v-text-field
                label="닉네임"
                type="nickname"
                v-model="nickname"
                :rules="[v=>!!v || '닉네임 입력은 필수입니다.']"
                required
              />
              <v-checkbox
                label="약관에 동의합니다."
                v-model="terms"
                :rules="[v=>!!v || '약관에 동의해야 합니다.']"
                required
              />
              <v-card-actions>
                <v-spacer />
                <v-btn color="deep-orange" :disabled="!valid" type="submit">가입하기</v-btn>
              </v-card-actions>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

export default {
  data() {
    return {
      valid: false,
      email: "",
      password: "",
      passwordCheck: "",
      showPassword: false,
      showPasswordCheck: false,
      nickname: "",
      terms: false,
      emailRules: [
        v => !!v || "이메일은 필수입니다.",
        v => re.test(v) || "이메일이 유효하지 않습니다."
      ],
      passwordRules: [v => !!v || "비밀번호를 입력해 주시기 바랍니다."],
      passwordCheckRules: [
        v => !!v || "비밀번호를 확인은 필수입니다.",
        v => v === this.password || "비밀번호가 일치하지 않습니다."
      ]
    };
  },
  methods: {
    onSubmitForm() {
      if (this.$refs.form.validate()) {
        //console.log("유효한 가입 신청");
      } else {
        //console.log("유효하지 않음");
      }
    },
    checkPassword() {
      if (this.password) {
        this.passwordCheck = "";
      }
    }
  }
};
</script>

<style>
</style>