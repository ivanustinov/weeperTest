package ru.ustinov.sapertest.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.ustinov.sapertest.json.CharArraySerializer;
//import ru.ustinov.sapertest.json.MineHiddenCharArraySerializer;
import ru.ustinov.sapertest.model.GameInfoResponse;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */

public class Main {

    private final int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    public static void main(String[] args) throws JsonProcessingException {
        char[][] field = {
                {'x', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', 'x', ' '}
        };
//        PropertyFilter theFilter = new SimpleBeanPropertyFilter() {
//
//            @Override
//            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
//                if (include(writer)) {
//                    if (!writer.getName().equals("field")) {
//                        writer.serializeAsField(pojo, jgen, provider);
//                        return;
//                    }
//                    final Field f = (Field) pojo;
//                    if (!f.isCompleted()) {
//                        writer.serializeAsField(pojo, jgen, provider);
//                    }
//
//                } else if (!jgen.canOmitFields()) { // since 2.3
//                    writer.serializeAsOmittedField(pojo, jgen, provider);
//                }
//            }
//
//            @Override
//            protected boolean include(BeanPropertyWriter writer) {
//                return true;
//            }
//
//            @Override
//            protected boolean include(PropertyWriter writer) {
//                return true;
//            }
//        };

//        FilterProvider filters = new SimpleFilterProvider().addFilter("mineArrayFilter", theFilter);

//        field.setCompleted(true);

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(char[].class, new CharArraySerializer());
//        module.addSerializer(char[].class, new MineHiddenCharArraySerializer());
        objectMapper.registerModule(module);
//        String dtoAsString = objectMapper.writer(filters).writeValueAsString(field);
        GameInfoResponse gameInfoResponseTotal = new GameInfoResponse(5, 5, 2);
        openCell(gameInfoResponseTotal.getMarked(), gameInfoResponseTotal.getForPlayer(), 2, 2, gameInfoResponseTotal.getCountOfLeftCells());

        System.out.println(objectMapper.writeValueAsString(gameInfoResponseTotal));
        
        
//        char[] chars = {'0', '0', '1', '1', '1', '0', '0', '0', '0', '0'};
//
//
//
//        String json = objectMapper.writeValueAsString(field.getField());
//
//        System.out.println(json);
    }

    public static int openCell(char[][] marked, char[][] forPlayer, int row, int col, int countOfLeftCells) {
        int numRows = marked.length, numCols = marked[0].length;
        if (row >= 0 && row < numRows && col >= 0 && col < numCols && marked[row][col] != forPlayer[row][col]) {
            // Открываем ячейку в соответствии с количеством мин вокруг
            forPlayer[row][col] = marked[row][col];
            // Убавляем счетик оставшихся ячеек игрового поля
            countOfLeftCells--;
            // Если нет вокруг мин и игра не закончилась, начинаем рекурсивный обход соседних ячеек
            if (forPlayer[row][col] == '0' && countOfLeftCells != 0) {
                final int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
                for (int[] dir : directions) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];
                    countOfLeftCells = openCell(marked, forPlayer, newRow, newCol, countOfLeftCells);
                }
            }
        }
        return countOfLeftCells;
    }


//        Field fieldTotal = new Field( 5, 5, 2);
//        Main main = new Main();
//        final int countOfLeftCells = fieldTotal.getCountOfLeftCells();
//        System.out.println("TotalCellsLeft before turn = " + countOfLeftCells);
//        final int i = main.openCell(field, 2, 2, countOfLeftCells);
//        for (char[] chars : field) {
//            System.out.println(Arrays.toString(chars));
//        }
//        System.out.println("TotalCellsLeft after turn = " + i);
//    }

//    public static class FieldSerializationModule extends SimpleModule {
//
//        public FieldSerializationModule() {
//            addSerializer(char[][].class, new MineHiddenArraySerializer());
//        }
//    }

}

