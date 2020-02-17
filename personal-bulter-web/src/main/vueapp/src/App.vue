<template>
  <div>
    <mc-layout></mc-layout>
  </div>
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
