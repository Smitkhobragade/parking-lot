package service;

import config.DatabaseConfig;

import java.sql.Connection;

public class VehicleService {
    private final Connection connection;
    private final SlotService slotService;

    public VehicleService(SlotService slotService) {
        this.connection = DatabaseConfig.getInstance().getConnection();
        this.slotService = slotService;
    }
}
