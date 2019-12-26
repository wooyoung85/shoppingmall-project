<template>
  <div>
    <v-app v-if="!isMainPage()">
      <v-app-bar app color="deep-orange lighten-2" dark>
        <div class="nav-spacer"></div>
        <v-toolbar-title>
          <router-link to="/"><img src="@/assets/images/main/main_page_logo.svg" style="margin-top:5px" /></router-link>
        </v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn text rounded to="/product">Products</v-btn>
        <v-btn text rounded :to="{ name:'register' }">Register</v-btn>
        <div class="nav-spacer"></div>
      </v-app-bar>
      <v-content>
        <v-container>
          <router-view />
        </v-container>
      </v-content>
    </v-app>
    <router-view v-else />
  </div>
</template>

<script>
import MainPage from '@/views/MainPage.vue'

export default {
  data: () => ({
    //
  }),
  methods: {
    isMainPage() {
      var isMain = false;
      if (window.location.pathname === "/") {
        isMain = true;
      }
      return isMain;
    }
  }
};
</script>
<style>
.nav-spacer{
    flex-grow: 0.2 !important;
}
</style>