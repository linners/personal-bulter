<template>
  <mc-layout id="app"></mc-layout>
</template>

<script>
  import McLayout from "@/components/layout/McLayout";

  export default {
    name: 'app',
    components: {McLayout},
    data() {
      return {
        outVisit: false,
        showLayout: false
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

<style>
  @import "./assets/common.css";
</style>
