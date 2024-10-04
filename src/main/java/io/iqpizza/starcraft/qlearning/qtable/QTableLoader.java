package io.iqpizza.starcraft.qlearning.qtable;

import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.BuildingType;
import io.iqpizza.starcraft.qlearning.protocol.State;
import io.iqpizza.starcraft.qlearning.utils.ActionUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Slf4j
public class QTableLoader {

    private QTableLoader() {
        throw new IllegalAccessError("Utility class");
    }

    // 특정 디렉토리 내 모든 CSV 파일을 읽어서 QTable 목록으로 반환
    public static List<QTable> loadAllQTablesFromDirectory(String directoryPath) {
        List<QTable> qTables = new ArrayList<>();

        // 디렉토리 내 파일 목록을 가져옴
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            log.warn("Directory is not a directory: {}", directoryPath);
            return qTables;
        }

        // 디렉토리 내의 파일 중 확장자가 .csv인 파일들만 필터링
        File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (csvFiles != null) {
            for (File csvFile : csvFiles) {
                // 각 CSV 파일에 대해 기존 메소드를 호출하여 QTable을 읽어옴
                String filePath = csvFile.getAbsolutePath();
                QTable qTable = loadQTableFromCsv(filePath);
                qTables.add(qTable);
            }
        } else {
            log.warn("There are no csv files yet. {}", directoryPath);
        }

        return qTables;
    }

    // CSV 파일에서 Q-테이블을 불러오는 메서드
    public static QTable loadQTableFromCsv(String filename) {
        QTable qTable = new QTable();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // 첫 줄은 헤더이므로 건너뜀

            while ((line = reader.readLine()) != null) {
                // CSV 파일의 각 줄을 "상태, 행동, Q-값"으로 분리
                String[] tokens = line.split(",");

                // 상태 역직렬화
                State state = deserializeState(tokens);
                ActionType action = ActionUtils.toActionType(tokens[tokens.length - 2]);  // Abilities name을 다시 ActionType으로 변환
                double qValue = Double.parseDouble(tokens[tokens.length - 1]);

                // Q-테이블에 상태, 행동, Q-값 추가
                qTable.setQValue(state, action, qValue);
            }

            log.info("Q-Table was successfully imported from CSV file.");
        } catch (IOException e) {
            log.error("Error occurs while read q-table into csv file: {}", filename, e);
        }

        return qTable;
    }

    // CSV로부터 상태(State)를 역직렬화하는 메서드
    private static State deserializeState(String[] tokens) {
        int minerals = Integer.parseInt(tokens[0]);
        int gas = Integer.parseInt(tokens[1]);
        int supplyUsed = Integer.parseInt(tokens[2]);
        int supplyMax = Integer.parseInt(tokens[3]);
        int workerSupply = Integer.parseInt(tokens[4]);
        int armySupply = Integer.parseInt(tokens[5]);
        Map<UnitType, Integer> myUnits = deserializeUnitTypeMap(tokens[6]);
        Map<UnitType, Integer> enemyUnits = deserializeUnitTypeMap(tokens[7]);
        Map<BuildingType, Integer> myBuildings = deserializeBuildingTypeMap(tokens[8]);
        Point2d enemyBaseLocation = Point2d.of(Float.parseFloat(tokens[9]), Float.parseFloat(tokens[10]));
        Race myRace = Race.valueOf(tokens[11]);
        Race enemyRace = Race.valueOf(tokens[12]);

        return new State(minerals, gas, supplyUsed, supplyMax, workerSupply, armySupply, myUnits, enemyUnits, myBuildings, enemyBaseLocation, myRace, enemyRace);
    }

    // Map을 역직렬화하는 메서드
    private static Map<UnitType, Integer> deserializeUnitTypeMap(String token) {
        Map<UnitType, Integer> map = new HashMap<>();
        String[] entries = token.split(";");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            String unitName = keyValue[0].toUpperCase();

            int value = Integer.parseInt(keyValue[1]);
            map.put(Units.valueOf(unitName), value);
        }
        return map;
    }

    private static Map<BuildingType, Integer> deserializeBuildingTypeMap(String token) {
        Map<BuildingType, Integer> map = new EnumMap<>(BuildingType.class);
        String[] entries = token.split(";");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            String unitName = keyValue[0].toUpperCase();

            int value = Integer.parseInt(keyValue[1]);
            map.put(getBuildingType(unitName).orElse(null), value);
        }
        return map;
    }

    private static Optional<BuildingType> getBuildingType(String unitName) {
        for (BuildingType actionType : BuildingType.values()) {
            if (actionType.name().equals(unitName)) {
                return Optional.of(actionType);
            }
        }
        return Optional.empty();
    }

}
