package GetOrders;

import java.util.List;

public class GetListOrdersData {
    private List<OrdersElement> orders;
    private PageInfoElement pageInfo;
    private List<AvailableStationsElement> availableStations;

    public List<OrdersElement> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersElement> orders) {
        this.orders = orders;
    }

    public PageInfoElement getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoElement pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStationsElement> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStationsElement> availableStations) {
        this.availableStations = availableStations;
    }
}
