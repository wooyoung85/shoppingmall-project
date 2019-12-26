<template>
  <v-row>
    <v-col md="4">
      <div class="product-image">
        <v-img :src="productImage"></v-img>
      </div>
    </v-col>
    <v-col md="1"></v-col>
    <v-col md="7">
      <div class="product-info">
        <h1>{{ product.name }}</h1>
        <p v-if="product.inStock">재고 있음</p>
        <p v-else>
          재고
          <span class="indeed">없음</span>
        </p>

        <h3>용량</h3>
        <ul>
          <li v-for="(volume,index) in volumes" :key="index">{{ volume }}</li>
        </ul>

        <h3>가입유형</h3>
        <ul>
          <li v-for="(regType,index) in regTypes" :key="index">{{ regType }}</li>
        </ul>
      </div>
    </v-col>
  </v-row>
</template>

<script>
const w_product = null;

export default {
  data() {
    return {
      product: null,
      regTypes: ["기기변경", "번호이동", "신규가입"],
      volumes: ["32GB", "64GB", "128GB", "512GB"]
    };
  },
  computed: {
    productImage() {
      return require("@/assets/images/product/" + this.product.id + "/" + this.product.variants[0].variantImage + "_13.jpg");
    }
  },
  created() {
    this.product = this.$store.state.products.filter(
      p => p.id === this.$route.params.productId
    )[0];
  }
};
</script>

<style>
</style>