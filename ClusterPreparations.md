# OpenShift Cluster Preparations

1. Ensure that the cluster's `project-default` template is configured to allow SonarQube to have more that 2GB of RAM
   1. From the CLI logged in a `system:admin`: `oc edit template project-request -n default`
   1. Remove the `RangeLimit` section