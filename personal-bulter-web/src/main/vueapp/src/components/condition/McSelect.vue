<template>
  <div style="display: table; padding-right: 10px;padding-bottom: 10px;min-width: 250px">
    <span class="search-label">{{label}}</span>
    <slot>
      <el-select style="display: table-cell; width: 100%;" v-model="selectVal" placeholder="请选择" size="small" filterable clearable @change="change">
        <el-option
          v-for="item in options"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
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

			}
		},
		mounted() {

		},
    computed: {
      selectVal: {
        get() {
          return this.value
        },
        set(val) {
          this.$emit("update:value", val)
        }
      }
    },
		watch: {

		},
		methods: {
      change(val) {
        this.$emit("change", val)
      }
    }
	}
</script>

<style scoped>

</style>
