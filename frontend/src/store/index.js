import Vue from "vue";
import Vuex from "vuex";
import ProductService from '@/services/ProductService.js'

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    products: [],
    user: { id: 'wooyoung85', name: 'WooYoung SEO' },
    cities: ['서울', '대전', '대구', '부산', '광주', '울산'],
    cart: 0,
  },
  mutations: {
    SET_PRODUCTS(state, products) {
      state.products = products
    },
    ADD_CART(state, value) {
      state.cart += value
    }
  },
  actions: {
    fetchProducts({ commit }) {
      ProductService.getCellphones()
        .then(response => {
          commit('SET_PRODUCTS', response.data)
        })
        .catch(error => {
          console.log('There was an error:', error.response)
        })
    }
  },
  getters: {
    citiesLength: state => {
      return state.cities.length
    }
  }
});

export default store;