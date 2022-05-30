<template>
  <div>
    <div class="container">
      <b-form @submit="onSubmit" @reset="onReset" v-if="show">
        <b-form-group id="input-group-2" label="Your Name:" label-for="input-2">
          <b-form-input
            id="input-2"
            v-model="payload.summonerName"
            placeholder="Enter Summoner Name"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-3" label="Region:" label-for="input-3">
          <b-form-select
            id="input-3"
            v-model="payload.region"
            :options="regions"
            required
          ></b-form-select>
        </b-form-group>

        <b-button type="submit" variant="primary">Submit</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      payload: {
        summonerName: "",
        region: null,
      },
      regions: [{ text: "Select Region", value: null }, "NA", "KR"],
      show: true,
    };
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      this.$store.dispatch("getUserinfo", this.payload);
    },
  },
};
</script>

<style></style>
