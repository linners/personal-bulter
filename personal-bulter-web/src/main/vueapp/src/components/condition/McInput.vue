<template>
  <div style="display: table; padding-right: 10px; padding-bottom: 10px; min-width: 250px">
    <span class="search-label">{{label}}</span>
    <slot>
      <el-input style="display: table-cell;width: 100%;" v-model="inputval" placeholder="请输入内容" size="small"></el-input>
    </slot>
  </div>
</template>

<script>
	export default {
		name: "mc-input",
		components: {},
		props: {
      label: {
        type: String,
        default: ""
      },
      value: {
        // required: true
      }
    },
		data() {
			return {

			}
		},
    computed: {
      inputval: {
        get() {
		      return this.value
        },
        set(val) {
          this.$emit("update:value", val)
        }
      }
    },
		mounted() {

		},
		watch: {

		},
		methods: {

    }
	}
</script>

<style scoped>

</style>
