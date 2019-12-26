<template>
  <div>
    <v-row>
      <v-col cols="12" md="4">
        <h3>제조사</h3>
        <v-btn-toggle v-model="companyList" tile color="deep-purple accent-3" group multiple>
          <v-btn value="samsung">삼성</v-btn>
          <v-btn value="apple">Apple</v-btn>
          <v-btn value="lg">LG</v-btn>
          <v-btn value="etc">기타</v-btn>
        </v-btn-toggle>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="12" md="4">
        <h3>할인 방법</h3>
        <v-select
          :value="selectedSupport"
          :items="support"
          menu-props="auto"
          label="선택"
          color="purple lighten-3"
          prepend-icon="mdi-filter"
          single-line
          filled
          @change="onChangeSupport"
        ></v-select>
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-col
        cols="12"
        md="3"
        class="pa-1 pt-5"
        v-for="(product, index) in productList"
        :key="index"
      >
        <product-card-component :product="product" />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import ProductCardComponent from "../components/ProductCardComponent";
import ProductService from '@/services/ProductService.js'

export default {
  components: { ProductCardComponent },
  data() {
    return {
      companyList: ["samsung", "apple"],
      support: [
        {
          text: "전체",
          value: "all"
        },
        {
          text: "복지포인트 사용",
          value: "welfare"
        },
        {
          text: "캐시백 환급",
          value: "cashbag"
        }
      ],
      products: [],
      selectedSupport: "all"
    };
  },
  computed: {
    productList() {
      if (this.selectedSupport !== "all") {
        return this.$store.state.products.filter(
          p =>
            this.companyList.includes(p.brand) &&
            p.support === this.selectedSupport
        );
      } else {
        return this.$store.state.products.filter(
          p =>
            this.companyList.includes(p.brand)
        );
      }
    }
  },
  methods: {
    onChangeSupport(value) {
      this.selectedSupport = value;
    }
  },
  // created() {
  //   ProductService.getCellphones()
  //     .then(response => {
  //       this.products = response.data;
  //     })
  //     .catch(error => {
  //       //console.log("Error Message:", error.response)
  //     })
  // }
  created() {
    this.$store.dispatch('fetchProducts')
  },
};
</script>

<style>
</style>