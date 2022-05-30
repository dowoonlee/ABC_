<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png" />
    <main-page></main-page>
  </div>
</template>

<script>
// @ is an alias to /src
import MainPage from "@/components/MainPage.vue";

export default {
  name: "HomeView",
  components: {
    MainPage,
  },
};
</script>
