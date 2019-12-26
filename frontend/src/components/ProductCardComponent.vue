<template>
  <v-card class="mx-auto" min-height="450px">
    <v-img height="300px" :src="productImage">
    </v-img>
    <v-card-title style="color:black">{{ product.name }}</v-card-title>
    <v-card-text class="text--primary" style="height:100px">
      <p>{{ product.description }}</p>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="orange" text>Share</v-btn>
      <v-btn color="orange" text :to="{ name: 'productDetail', params: { productId: this.product.id }}">Explore</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  props: {
    product: { type: Object, required: true }
  },

  computed: {
    productImage() {
      return require("@/assets/images/product/" + this.product.id + "/" + this.product.variants[0].variantImage + "_13.jpg");
    }
  }
};
</script>

<style>
</style>