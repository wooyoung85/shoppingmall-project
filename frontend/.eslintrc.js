module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: ["plugin:vue/strongly-recommended"],
  rules: {
    "no-console": process.env.NODE_ENV === "production" ? "error" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    "vue/max-attributes-per-line" : "off",
    "vue/singleline-html-element-content-newline" : "off",
    "vue/attributes-order" : "off",
    "vue/html-self-closing" : "off"
  },
  parserOptions: {
    parser: "babel-eslint"
  }
};
