package cn.edu.wic.zzy.pureclock.bean;

import java.util.List;

/**
 * Created by sky on 10/14/16.
 */

public class CityInfoBean {

    /**
     * status : 1
     * info : OK
     * infocode : 10000
     * regeocode : {"formatted_address":"北京市朝阳区望京街道方恒国际中心B座方恒国际中心","addressComponent":{"country":"中国","province":"北京市","city":[],"citycode":"010","district":"朝阳区","adcode":"110105","township":"望京街道","towncode":"110105026000","neighborhood":{"name":"方恒国际中心","type":"商务住宅;楼宇;商住两用楼宇"},"building":{"name":"方恒国际中心B座","type":"商务住宅;楼宇;商务写字楼"},"streetNumber":{"street":"阜通东大街","number":"6-2号楼","location":"116.48129,39.9902869","direction":"西南","distance":"25.9205"},"businessAreas":[{"location":"116.47089234140496,39.9976009239991","name":"望京","id":"110105"},{"location":"116.47305065693433,39.98350093430656","name":"花家地","id":"110105"},{"location":"116.48976000793644,39.984900765873","name":"大山子","id":"110105"}]}}
     */

    private String status;
    private String info;
    private String infocode;
    /**
     * formatted_address : 北京市朝阳区望京街道方恒国际中心B座方恒国际中心
     * addressComponent : {"country":"中国","province":"北京市","city":[],"citycode":"010","district":"朝阳区","adcode":"110105","township":"望京街道","towncode":"110105026000","neighborhood":{"name":"方恒国际中心","type":"商务住宅;楼宇;商住两用楼宇"},"building":{"name":"方恒国际中心B座","type":"商务住宅;楼宇;商务写字楼"},"streetNumber":{"street":"阜通东大街","number":"6-2号楼","location":"116.48129,39.9902869","direction":"西南","distance":"25.9205"},"businessAreas":[{"location":"116.47089234140496,39.9976009239991","name":"望京","id":"110105"},{"location":"116.47305065693433,39.98350093430656","name":"花家地","id":"110105"},{"location":"116.48976000793644,39.984900765873","name":"大山子","id":"110105"}]}
     */

    private RegeocodeBean regeocode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public RegeocodeBean getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(RegeocodeBean regeocode) {
        this.regeocode = regeocode;
    }

    public static class RegeocodeBean {
        private String formatted_address;
        /**
         * country : 中国
         * province : 北京市
         * city : []
         * citycode : 010
         * district : 朝阳区
         * adcode : 110105
         * township : 望京街道
         * towncode : 110105026000
         * neighborhood : {"name":"方恒国际中心","type":"商务住宅;楼宇;商住两用楼宇"}
         * building : {"name":"方恒国际中心B座","type":"商务住宅;楼宇;商务写字楼"}
         * streetNumber : {"street":"阜通东大街","number":"6-2号楼","location":"116.48129,39.9902869","direction":"西南","distance":"25.9205"}
         * businessAreas : [{"location":"116.47089234140496,39.9976009239991","name":"望京","id":"110105"},{"location":"116.47305065693433,39.98350093430656","name":"花家地","id":"110105"},{"location":"116.48976000793644,39.984900765873","name":"大山子","id":"110105"}]
         */

        private AddressComponentBean addressComponent;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public AddressComponentBean getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentBean addressComponent) {
            this.addressComponent = addressComponent;
        }

        public static class AddressComponentBean {
            private String country;
            private String province;
            private String citycode;
            private String district;
            private String adcode;
            private String township;
            private String towncode;
            /**
             * name : 方恒国际中心
             * type : 商务住宅;楼宇;商住两用楼宇
             */

            private NeighborhoodBean neighborhood;
            /**
             * name : 方恒国际中心B座
             * type : 商务住宅;楼宇;商务写字楼
             */

            private BuildingBean building;
            /**
             * street : 阜通东大街
             * number : 6-2号楼
             * location : 116.48129,39.9902869
             * direction : 西南
             * distance : 25.9205
             */

            private StreetNumberBean streetNumber;
            private List<?> city;
            /**
             * location : 116.47089234140496,39.9976009239991
             * name : 望京
             * id : 110105
             */

            private List<BusinessAreasBean> businessAreas;

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCitycode() {
                return citycode;
            }

            public void setCitycode(String citycode) {
                this.citycode = citycode;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getTownship() {
                return township;
            }

            public void setTownship(String township) {
                this.township = township;
            }

            public String getTowncode() {
                return towncode;
            }

            public void setTowncode(String towncode) {
                this.towncode = towncode;
            }

            public NeighborhoodBean getNeighborhood() {
                return neighborhood;
            }

            public void setNeighborhood(NeighborhoodBean neighborhood) {
                this.neighborhood = neighborhood;
            }

            public BuildingBean getBuilding() {
                return building;
            }

            public void setBuilding(BuildingBean building) {
                this.building = building;
            }

            public StreetNumberBean getStreetNumber() {
                return streetNumber;
            }

            public void setStreetNumber(StreetNumberBean streetNumber) {
                this.streetNumber = streetNumber;
            }

            public List<?> getCity() {
                return city;
            }

            public void setCity(List<?> city) {
                this.city = city;
            }

            public List<BusinessAreasBean> getBusinessAreas() {
                return businessAreas;
            }

            public void setBusinessAreas(List<BusinessAreasBean> businessAreas) {
                this.businessAreas = businessAreas;
            }

            public static class NeighborhoodBean {
                private String name;
                private String type;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class BuildingBean {
                private String name;
                private String type;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class StreetNumberBean {
                private String street;
                private String number;
                private String location;
                private String direction;
                private String distance;

                public String getStreet() {
                    return street;
                }

                public void setStreet(String street) {
                    this.street = street;
                }

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getDirection() {
                    return direction;
                }

                public void setDirection(String direction) {
                    this.direction = direction;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }
            }

            public static class BusinessAreasBean {
                private String location;
                private String name;
                private String id;

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }
    }
}
