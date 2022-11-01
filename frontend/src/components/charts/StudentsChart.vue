<script>
    import { defineComponent } from 'vue'
    import { Bar } from 'vue3-chart-v2'

    export default defineComponent({
        name: 'StudentsChart',
        extends: Bar,
        props: {
            main_label: String,
            data: Array,
            mth: Array
        },
        methods: {
            refresh(){
                console.log("tt", this.data);
                if(this.state.chartObj)
                    this.state.chartObj.destroy()
                this.renderChart({

                    labels: this.mth,
                    datasets: [
                        {
                            label:  this.main_label,
                            backgroundColor: '#409EFF',
                            data: this.data
                        }
                    ]
                })
            }
        },
        mounted () {
            // Overwriting base render method with actual data.

        }
    })
</script>