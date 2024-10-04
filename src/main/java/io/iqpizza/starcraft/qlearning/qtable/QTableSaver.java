package io.iqpizza.starcraft.qlearning.qtable;

import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;
import io.iqpizza.starcraft.qlearning.utils.QTableUtils;
import io.iqpizza.starcraft.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class QTableSaver {

    private QTableSaver() {
        throw new AssertionError();
    }

    public static void saveQTableToCsv(QTable table, String filename) {
        File file = new File(filename);
        // 상위 디렉토리 확인 및 생성
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                log.trace("Directory has been created: {}", parentDir.getAbsolutePath());
            } else {
                log.error("Directory could not be created: {}", parentDir.getAbsolutePath());
                return;
            }
        }

        try (final FileWriter writer = new FileWriter(file)) {

            // CSV 헤더 작성
            writer.append("State,Action,Q-Value\n");

            // Q-테이블을 순회하며 상태, 행동, Q-값을 CSV로 저장
            for (Map.Entry<State, Map<ActionType, Double>> stateEntry : table.getTable().entrySet()) {
                State state = stateEntry.getKey();
                for (Map.Entry<ActionType, Double> actionEntry : stateEntry.getValue().entrySet()) {
                    ActionType action = actionEntry.getKey();
                    double qValue = actionEntry.getValue();
                    // 각 상태, 행동, Q-값을 CSV의 한 줄로 작성
                    writer.append(serializeState(state)).append(",").append(action.getString()).append(",").append(String.valueOf(qValue)).append("\n");
                }
            }

            log.info("The Q-Table has been successfully saved as a CSV file.");
        } catch (IOException e) {
            log.error("Error occurs while writing q-table into csv file: {}", filename, e);
        }
    }

    public static void tryMergeToSingleCsv(String filename) {
        if (countCsvFiles(filename) < 10) {
            log.debug("아직 10개가 되지 않아서 병합하지 않습니다.");
            return;
        }

        String directoryPath = PropertiesUtils.getVariable(PropertiesUtils.PropertyVariable.CSV);
        List<QTable> qTables = QTableLoader.loadAllQTablesFromDirectory(directoryPath);
        if (qTables.isEmpty()) {
            log.debug("실제 저장된 파일이 10개가 아닙니다. 병합을 취소합니다.");
            return;
        }

        QTable mergeQTables = QTableUtils.mergeQTables(qTables);
        mergeToSingleCsv(mergeQTables, filename);

        deleteOtherCsvFiles(directoryPath);
    }

    private static int countCsvFiles(String filename) {
        File file = new File(filename);
        // 상위 디렉토리 확인 및 생성
        File parentDir = file.getParentFile();
        if (parentDir == null || !parentDir.exists()) {
            throw new IllegalStateException("Directory could not be created");
        }

        FilenameFilter csvFilter = (dir, name) -> name.toLowerCase().endsWith(".csv");

        // 해당 디렉토리에서 .csv 확장자를 가진 파일 목록을 가져옴
        File[] csvFiles = parentDir.listFiles(csvFilter);

        // CSV 파일의 갯수를 반환
        return csvFiles != null ? csvFiles.length : 0;
    }

    private static void mergeToSingleCsv(QTable mergedTable, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // CSV 헤더 작성
            writer.append("State,Action,Q-Value\n");

            // 병합된 Q-테이블 데이터를 CSV로 저장
            for (Map.Entry<State, Map<ActionType, Double>> stateEntry : mergedTable.getTable().entrySet()) {
                State state = stateEntry.getKey();
                for (Map.Entry<ActionType, Double> actionEntry : stateEntry.getValue().entrySet()) {
                    ActionType action = actionEntry.getKey();
                    double qValue = actionEntry.getValue();
                    writer.append(serializeState(state)).append(",")
                            .append(action.getAbility().name()).append(",")
                            .append(String.valueOf(qValue)).append("\n");
                }
            }

            log.info("The Q-Table has been successfully merged as a CSV file.");
        } catch (IOException e) {
            log.error("Error occurs while writing q-table into csv file: {}", filename, e);
        }
    }

    // 병합된 CSV 파일을 제외하고 나머지 파일을 삭제하는 메서드
    public static void deleteOtherCsvFiles(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".csv"));

        if (files != null) {
            for (File file : files) {
                // 병합된 파일은 제외하고 나머지 CSV 파일들을 삭제
                if (!file.getName().endsWith("merged.csv") && !file.delete()) {
                    log.warn("{} file could not be deleted.", file.getName());
                }
            }
        }
    }

    // State 객체를 직렬화하여 문자열로 변환하는 메서드
    private static String serializeState(State state) {
        // State의 각 필드를 콤마로 구분하여 문자열로 변환
        return state.minerals() + "," + state.gas() + "," + state.supplyUsed() + "," + state.supplyMax() + ","
                + state.workerSupply() + "," + state.armySupply() + "," + serializeMap(state.myUnits()) + ","
                + serializeMap(state.enemyUnits()) + "," + serializeMap(state.myBuildings()) + ","
                + state.enemyBaseLocation().getX() + "," + state.enemyBaseLocation().getY() + ","
                + state.myRace().name() + "," + state.enemyRace().name();
    }

    // Map을 직렬화하는 메서드 (UnitType, BuildingType 등의 맵을 처리)
    private static <T> String serializeMap(Map<T, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey().toString()).append(":").append(entry.getValue()).append(";");
        }
        return sb.toString();
    }
}
