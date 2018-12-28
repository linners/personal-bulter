<template>
  <div style="display: table; padding-right: 10px;padding-bottom: 10px;min-width: 250px">
    <span class="search-label">{{label}}</span>
    <slot>
      <el-cascader style="display: table-cell; width: 100%;" size="small" filterable change-on-select clearable
        :options="options"
        v-model="selectval"
        @change="handleChange">
      </el-cascader>
    </slot>
  </div>
</template>

<script>
	export default {
		name: "mc-select",
		components: {},
		props: {
      label: {
        type: String,
        default: ""
      },
      options: {
        type: Array,
        default() {
          return []
        }
      },
      value: {
        required: true
      }
    },
		data() {
			return {
        options1: []
			}
		},
    computed: {
      selectval: {
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
      handleChange(){

      }
    }
	}
</script>

<style scoped>

</style>
