<template>
  <div>
    <svg-icon icon-class="question" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'lewisDoc',
  data() {
    return {
      url: 'http://doc.lewis.vip/lewis-vue'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>