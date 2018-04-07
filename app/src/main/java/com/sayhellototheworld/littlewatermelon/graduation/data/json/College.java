package com.sayhellototheworld.littlewatermelon.graduation.data.json;

import java.util.List;

/**
 * Created by 123 on 2018/4/7.
 */

public class College {


    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String departId;
        private String departName;
        private List<CollegeLocationsBean> collegeLocations;

        public String getDepartId() {
            return departId;
        }

        public void setDepartId(String departId) {
            this.departId = departId;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public List<CollegeLocationsBean> getCollegeLocations() {
            return collegeLocations;
        }

        public void setCollegeLocations(List<CollegeLocationsBean> collegeLocations) {
            this.collegeLocations = collegeLocations;
        }

        public static class CollegeLocationsBean {

            private String locationId;
            private String departId;
            private String locationName;
            private List<CollegeNamesBean> collegeNames;

            public String getLocationId() {
                return locationId;
            }

            public void setLocationId(String locationId) {
                this.locationId = locationId;
            }

            public String getDepartId() {
                return departId;
            }

            public void setDepartId(String departId) {
                this.departId = departId;
            }

            public String getLocationName() {
                return locationName;
            }

            public void setLocationName(String locationName) {
                this.locationName = locationName;
            }

            public List<CollegeNamesBean> getCollegeNames() {
                return collegeNames;
            }

            public void setCollegeNames(List<CollegeNamesBean> collegeNames) {
                this.collegeNames = collegeNames;
            }

            public static class CollegeNamesBean {
                /**
                 * locationId : 1
                 * name : 北京大学
                 */

                private String locationId;
                private String name;

                public String getLocationId() {
                    return locationId;
                }

                public void setLocationId(String locationId) {
                    this.locationId = locationId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
