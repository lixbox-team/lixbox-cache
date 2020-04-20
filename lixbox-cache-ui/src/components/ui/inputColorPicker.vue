<template>
  <div>
    <v-text-field
      :value="color"
      hide-details
      class="ma-0 pa-0"
      solo
      v-on:input="setColorSelected($event)"
    >
      <template v-slot:append>
        <v-menu
          v-model="menu"
          top
          nudge-bottom="105"
          nudge-left="16"
          :close-on-content-click="false"
        >
          <template v-slot:activator="{ on }">
            <div :style="swatchStyle" v-on="on" />
          </template>
          <v-card>
            <v-card-text class="pa-0" id="rest">
              <v-color-picker :value="color" v-on:input="setColorSelected($event)" />
            </v-card-text>
          </v-card>
        </v-menu>
      </template>
    </v-text-field>
  </div>
</template>

<script>
export default {
  name: "inputColorPicker",
  props: {
    mask: {
      type: String,
      default: "!#XXXXXX"
    },
    value: {}
  },
  data: () => ({
    menu: false
  }),
  computed: {
    color() {
      return this.value;
    },
    swatchStyle() {
      const { value, menu } = this;
      return {
        backgroundColor: value,
        cursor: "pointer",
        height: "30px",
        width: "30px",
        borderRadius: menu ? "50%" : "4px",
        transition: "border-radius 200ms ease-in-out"
      };
    }
  },
  methods: {
    setColorSelected(_selectedColor) {
      this.$emit("input", _selectedColor);      
    }
  }
};
</script>

<style>
</style>