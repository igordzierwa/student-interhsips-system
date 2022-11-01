
let globalMixins = {
    data () {
        return {
            api: "http://localhost:9090"
        }
    },
    methods: {
        hasRole(roles){
            if( typeof this.userRole != "undefined") {
                console.log(roles)
                if(roles.indexOf("any") >= 0){
                    return true;
                }

                return roles.some(role => {
                    console.log(role)
                    return this.userRole.indexOf(role) >= 0;
                });
            }
            return false
        },
        message(dat){
          console.log("messa", dat)
        },
        valid(ref){
            var valid_value = false;
            console.log(this.$refs, ref);
            this.$refs[ref].validate((valid) => {
                if (valid) {
                    valid_value = true;
                    return true;
                } else {
                    return false;
                }
            });
            return valid_value;
        },
    }
}
export default globalMixins;