package edu.trucktrack.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import com.vaadin.collaborationengine.LicenseStorage;
import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.dao.entity.CurrencyEntity;
import edu.trucktrack.dao.entity.EmployeeExpensesEntity;
import edu.trucktrack.dao.entity.TagEntity;
import edu.trucktrack.dao.entity.WorkTripEntity;
import edu.trucktrack.dao.entity.WorkTripSalaryHistoryEntity;
import edu.trucktrack.dao.repository.jpa.CurrencyJpaRepository;
import edu.trucktrack.dao.repository.jpa.ExpensesJpaRepository;
import edu.trucktrack.dao.repository.jpa.WorkTripJpaRepository;
import edu.trucktrack.dao.repository.jpa.WorkTripSalaryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReportUtil {

	private final WorkTripJpaRepository workTripJpaRepository;
	private final ExpensesJpaRepository expensesJpaRepository;
	private final CurrencyJpaRepository currencyJpaRepository;

	private final String LABEL = "Driver Report";
	private final String DATE_RANGE = "Date range: %s - %s";
	private final String DRIVER_NAME = "Driver name: %s";
	private final String TRUCK_MODEL = "Truck model: %s";
	private final String TRUCK_NUMBER = "Truck number: %s";
	private final String DRIVER_COSTS = "Driver costs: %s";
	private final String TRUCK_COSTS = "Truck costs: %s";
	private final String DRIVER_SALARY = "Driver salary: %s";


	public File getPdfReport(WorkTripEntity workTrip, Map<EmployeeExpensesDTO, List<String>> expensesTagMap) {
		File file = new File(workTrip.getEmployee().getName() + "-" + LocalDate.now() + ".pdf");

		try (OutputStream stream = new FileOutputStream(file)){

			Document document = new Document();
			PdfWriter.getInstance(document, stream);

			document.open();
			document.add(new Paragraph(18, String.format(LABEL)));
			document.add(new Paragraph(String.format(DATE_RANGE, workTrip.getCreatedAt().toLocalDate().toString(), "closed")));
			document.add(new Paragraph(String.format(DRIVER_NAME, workTrip.getEmployee().getName())));
			document.add(new Paragraph(String.format(TRUCK_MODEL, workTrip.getTruck().getName())));
			document.add(new Paragraph(String.format(TRUCK_NUMBER, workTrip.getTruck().getTruckNumber())));
			document.add(new Paragraph(String.format(DRIVER_COSTS, expensesTagMap.entrySet().stream().filter(item -> item.getValue().contains("DRIVER")).map(item -> item.getKey().getValue()).mapToInt(Long::intValue).sum())));
			document.add(new Paragraph(String.format(TRUCK_COSTS, expensesTagMap.entrySet().stream().filter(item -> item.getValue().contains("TRUCK")).map(item -> item.getKey().getValue()).mapToInt(Long::intValue).sum())));
			document.add(new Paragraph(String.format(DRIVER_SALARY, workTrip.getSalary())));

			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));

			PdfPTable layoutTable = new PdfPTable(1);
			layoutTable.addCell(new Paragraph(18, "Driver expenses"));
			layoutTable.addCell(createPdfExpensesTable("DRIVER", expensesTagMap));
			layoutTable.addCell(new Paragraph(18, "Truck expenses"));
			layoutTable.addCell(createPdfExpensesTable("TRUCK", expensesTagMap));
			document.add(layoutTable);
			document.close();

			return file;
		} catch (Exception e) {
			return null;
		}
	}

	public File getCsvFile(WorkTripEntity workTrip, Map<EmployeeExpensesDTO, List<String>> expensesTagMap) {
		File file = new File(workTrip.getEmployee().getName() + "-" + LocalDate.now() + ".csv");

		try {

			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);


			writer.writeNext(new String[]{String.format(DATE_RANGE, workTrip.getCreatedAt().toLocalDate().toString(), "closed")});
			writer.writeNext(new String[]{String.format(DRIVER_NAME, workTrip.getEmployee().getName())});
			writer.writeNext(new String[]{String.format(TRUCK_MODEL, workTrip.getTruck().getName())});
			writer.writeNext(new String[]{String.format(TRUCK_NUMBER, workTrip.getTruck().getTruckNumber())});
			writer.writeNext(new String[]{String.format(DRIVER_COSTS, expensesTagMap.entrySet().stream().filter(item -> item.getValue().contains("DRIVER")).map(item -> item.getKey().getValue()).mapToInt(Long::intValue).sum())});
			writer.writeNext(new String[]{String.format(TRUCK_COSTS, expensesTagMap.entrySet().stream().filter(item -> item.getValue().contains("TRUCK")).map(item -> item.getKey().getValue()).mapToInt(Long::intValue).sum())});
			writer.writeNext(new String[]{String.format(DRIVER_SALARY, workTrip.getSalary())});

			writer.writeNext(new String[]{""});
			writer.writeNext(new String[]{""});


			writer.writeNext(new String[]{"", "Driver salary"});
			createCsvExpensesTable("DRIVER", expensesTagMap, writer);
			writer.writeNext(new String[]{""});
			writer.writeNext(new String[]{"", "Truck salary"});
			createCsvExpensesTable("TRUCK", expensesTagMap, writer);

			writer.close();
			return file;
		} catch (IOException e) {
			 return null;
		}
	}

	private PdfPTable createPdfExpensesTable(String expensesType, Map<EmployeeExpensesDTO, List<String>> expensesTagMap) {
		PdfPTable pdfPTable = new PdfPTable(4);
		pdfPTable.addCell("Name");
		pdfPTable.addCell("Description");
		pdfPTable.addCell("Value");
		pdfPTable.addCell("Currency");

		expensesTagMap.entrySet().stream().filter(item -> item.getValue().contains(expensesType)).forEach(key -> {
			pdfPTable.addCell(key.getKey().getName());
			pdfPTable.addCell(key.getKey().getDescription());
			pdfPTable.addCell(String.valueOf(key.getKey().getValue()));
			pdfPTable.addCell(key.getKey().getCurrency());
		});
		return pdfPTable;
	}

	private void createCsvExpensesTable(String expensesType, Map<EmployeeExpensesDTO, List<String>> expensesTagMap, CSVWriter writer) {

		writer.writeNext(new String[]{"Name", "Description", "Value", "Currency"});

		expensesTagMap.entrySet().stream().filter(item -> item.getValue().contains(expensesType)).forEach(key -> {
			writer.writeNext(new String[]{
			key.getKey().getName(),
			key.getKey().getDescription(),
			String.valueOf(key.getKey().getValue()),
			key.getKey().getCurrency()
			});
		});
	}

	public WorkTripEntity getWorkTrip(Long tripId) {
		return workTripJpaRepository.findById(tripId).get();
	}

	public Map<EmployeeExpensesDTO, List<String>> getExpensesTagMap (WorkTripEntity entity) {
		List<EmployeeExpensesEntity> expensesEntities = expensesJpaRepository.findAllByTripId(entity.getId());
		List<CurrencyEntity> currencyEntities = currencyJpaRepository.findAll();
		return expensesEntities.stream()
				.collect(Collectors.toMap(
						k -> EmployeeExpensesDTO.builder()
								.name(k.getName())
								.description(k.getDescription())
								.value(Long.valueOf(k.getValue()))
								.currency(String.valueOf(currencyEntities.stream().filter(i -> i.getId().equals(Long.valueOf(k.getCurrencyId()))).findFirst().get().getName())).build(),
						v -> v.getTags().stream().map(TagEntity::getName).toList()));
	}
}
