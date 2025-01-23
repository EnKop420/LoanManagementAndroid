package dk.rhww.loanmanagement;

// This is a MODEL Class
public class Tablet {
    public int id;
    public String tabletBrand;
    public String loanerName;
    public String loanedDate;
    public String cableType;

    public Tablet(String tabletBrand, String loanerName, String loanedDate, String cableType) {
        this.tabletBrand = tabletBrand;
        this.loanerName = loanerName;
        this.loanedDate = loanedDate;
        this.cableType = cableType;
    }

    // region Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTabletBrand() {
        return tabletBrand;
    }

    public void setTabletBrand(String tabletBrand) {
        this.tabletBrand = tabletBrand;
    }

    public String getLoanerName() {
        return loanerName;
    }

    public void setLoanerName(String loanerName) {
        this.loanerName = loanerName;
    }

    public String getLoanedDate() {
        return loanedDate;
    }

    public void setLoanedDate(String loanedDate) {
        this.loanedDate = loanedDate;
    }

    public String getCableType() {
        return cableType;
    }

    public void setCableType(String cableType) {
        this.cableType = cableType;
    }

    // endregion
}
