package com.troubleshooter.config;

public class ArchitectureConfig {

    private String controllerPackage;
    private String servicePackage;
    private String repositoryPackage;

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getRepositoryPackage() {
        return repositoryPackage;
    }

    public void setRepositoryPackage(String repositoryPackage) {
        this.repositoryPackage = repositoryPackage;
    }
}