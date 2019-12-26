<template>
  <div id="main-page">
    <header>
      <nav>
        <a href="/">
          <img class="logo" src="@/assets/images/main/main_page_logo.svg" alt="Happy Family Mall | Homepage" />
        </a>
        <div class="links">
          <a href="/about">About</a>
          <a href="/product">Products</a>
          <button class="button_med"><router-link to="/register">Register</router-link></button>
        </div>
      </nav>
    </header>
    <main>
      <section class="info">
        <h1 class="main_title">Happy Family Mall</h1>
        <h2 class="main_info">Only authorized users can use it.</h2>
        <button class="button_jumbo" href="#"><router-link to="/login">Login</router-link></button>
      </section>
      <section class="features">
        <div class="feature">
          <img src="@/assets/images/main/cellphone.png" alt="cellphone" />
          <div class="feature_info">
            <p class="feature_top">Cellphone</p>
            <p class="feature_bottom">Best price guarantee and Fast shipping</p>
          </div>
        </div>
        <div class="feature">
          <img src="@/assets/images/main/home_appliances.png" alt="Home Appliances" />
          <div class="feature_info">
            <p class="feature_top">Home Appliances</p>
            <p class="feature_bottom">Variety of products and best quality</p>
          </div>
        </div>
        <div class="feature">
          <img src="@/assets/images/main/accessory.png" alt="accessory" />
          <div class="feature_info">
            <p class="feature_top">Accessory</p>
            <p class="feature_bottom">Variety of products and best quality</p>
          </div>
        </div>
        <div class="feature">
          <div class="feature_info">
            <a class="feature_cta" href="#">View More</a>
            <p class="feature_bottom">Various items are available.</p>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
export default {};
</script>

<style scope src="@/assets/styles/main.css">
</style>