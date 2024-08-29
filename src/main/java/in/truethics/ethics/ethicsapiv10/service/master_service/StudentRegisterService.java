package in.truethics.ethics.ethicsapiv10.service.master_service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import in.truethics.ethics.ethicsapiv10.common.GenerateDates;
import in.truethics.ethics.ethicsapiv10.common.GenerateFiscalYear;
import in.truethics.ethics.ethicsapiv10.common.LedgerCommonPostings;
import in.truethics.ethics.ethicsapiv10.common.NumFormat;
import in.truethics.ethics.ethicsapiv10.fileConfig.FileStorageProperties;
import in.truethics.ethics.ethicsapiv10.fileConfig.FileStorageService;
import in.truethics.ethics.ethicsapiv10.model.history_table.*;
import in.truethics.ethics.ethicsapiv10.model.ledgers_details.LedgerBalanceSummary;
import in.truethics.ethics.ethicsapiv10.model.ledgers_details.LedgerTransactionDetails;
import in.truethics.ethics.ethicsapiv10.model.ledgers_details.LedgerTransactionPostings;
import in.truethics.ethics.ethicsapiv10.model.master.*;
import in.truethics.ethics.ethicsapiv10.model.sales.TranxSalesInvoice;
import in.truethics.ethics.ethicsapiv10.model.sales.TranxSalesInvoiceDetails;
import in.truethics.ethics.ethicsapiv10.model.school_master.*;
import in.truethics.ethics.ethicsapiv10.model.school_tranx.FeesTransactionSummary;
import in.truethics.ethics.ethicsapiv10.model.school_tranx.RightOffStudent;
import in.truethics.ethics.ethicsapiv10.model.tranx.journal.TranxJournalDetails;
import in.truethics.ethics.ethicsapiv10.model.tranx.journal.TranxJournalMaster;
import in.truethics.ethics.ethicsapiv10.model.user.Users;
import in.truethics.ethics.ethicsapiv10.repository.history_repository.*;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerBalanceSummaryRepository;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerMasterRepository;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerTransactionDetailsRepository;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerTransactionPostingsRepository;
import in.truethics.ethics.ethicsapiv10.repository.master_repository.*;
import in.truethics.ethics.ethicsapiv10.repository.student_tranx.FeesTransactionDetailRepository;
import in.truethics.ethics.ethicsapiv10.repository.student_tranx.FeesTransactionSummaryRepository;
import in.truethics.ethics.ethicsapiv10.repository.student_tranx.RightOffStudentRepository;
import in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.journal_repository.TranxJournalDetailsRepository;
import in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.journal_repository.TranxJournalMasterRepository;
import in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.sales_repository.TranxSalesInvoiceDetailsRepository;
import in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.sales_repository.TranxSalesInvoiceRepository;
import in.truethics.ethics.ethicsapiv10.response.ResponseMessage;
import in.truethics.ethics.ethicsapiv10.util.JwtTokenUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StudentRegisterService {
    private static final Logger studentLogger = LoggerFactory.getLogger(StudentRegisterService.class);
    @Autowired
    private JwtTokenUtil jwtRequestFilter;

    @Autowired
    private RightOffStudentRepository rightOffStudentRepository;
    @Autowired
    private StudentRegisterRepository studentRegisterRepository;
    @Autowired
    private OutletRepository outletRepository;
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private LedgerTransactionPostingsRepository ledgerTransactionPostingsRepository;
    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private FeesTransactionDetailRepository feesTransactionDetailRepository;


    @Autowired
    private TranxJournalDetailsRepository tranxJournalDetailsRepository;

    @Autowired
    private TranxJournalMasterRepository tranxJournalMasterRepository;

    @Autowired
    private DivisionRepository divisionRepository;
    @Autowired
    private StudentRegHistoryRepository studentRegHistoryRepository;

    @Autowired
    private StudentAdmHistoryRepository studentAdmHistoryRepository;
    @Autowired
    private StudentTransporHistoryRepository studentTransporHistoryRepository;

    @Autowired
    private FeesTranxSummaryHistoryRepository feesTranxSummaryHistoryRepository;

    @Autowired
    private FeesTranxDetailsHistoryRepository feesTranxDetailsHistoryRepository;

    @Autowired
    private LedgerBalanceSummaryHistoryRepository ledgerBalanceSummaryHistoryRepository;

    @Autowired
    private LedgerTranxDetailsHistoryRepository ledgerTranxDetailsHistoryRepository;
    @Autowired
    private LedgerMasterHistoryRepository ledgerMasterHistoryRepository;
    @Autowired
    private AcademicYearRepository academicYearRepository;
    @Autowired
    private FeesMasterRepository feesMasterRepository;
    @Autowired
    private ReligionRepository religionRepository;
    @Autowired
    private CasteRepository casteRepository;
    @Autowired
    private SubCasteRepository subCasteRepository;
    @Autowired
    private CasteCategoryRepository categoryRepository;
    @Autowired
    private MotherTongueRepository motherTongueRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private StudentAdmissionRepository admissionRepository;
    @Autowired
    private FeesTransactionSummaryRepository feesTransactionSummaryRepository;
    @Autowired
    private PrincipleRepository principleRepository;
    @Autowired
    private PrincipleGroupsRepository principleGroupsRepository;
    @Autowired
    private LedgerMasterRepository ledgerMasterRepository;
    @Autowired
    private LedgerBalanceSummaryRepository ledgerBalanceSummaryRepository;
    @Autowired
    private LedgerTransactionDetailsRepository ledgerTransactionDetailsRepository;
    @Autowired
    private TransactionTypeMasterRepository transactionTypeMasterRepository;

    @Autowired
    private StudentAdmissionRepository studentAdmissionRepository;
    @Autowired
    private GenerateFiscalYear generateFiscalYear;
    @Autowired
    private TranxSalesInvoiceRepository tranxSalesInvoiceRepository;
    @Autowired
    private FeesDetailsRepository feesDetailsRepository;
    @Autowired
    private FeeHeadRepository feeHeadRepository;
    @Autowired
    private NumFormat numFormat;

    @Autowired
    private SalesInvoiceDetailsHistoryRepository salesInvoiceDetailsHistoryRepository;

    @Autowired
    private SalesInvoiceHistoryRepository salesInvoiceHistoryRepository;

    @Autowired
    private JournalMasterHistoryRepository journalMasterHistoryRepository;

    @Autowired
    private JournalDetailsHistoryRepository journalDetailsHistoryRepository;
    @Autowired
    private TranxSalesInvoiceDetailsRepository tranxSalesInvoiceDetailsRepository;

    @Autowired
    private AssociateGroupsRepository associateGroupsRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private StudentTransportRepository studentTransportRepository;
    @Autowired
    private LedgerCommonPostings ledgerCommonPostings;
    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    private static String NumberToWord(String number) {
        String twodigitword = "";
        String word = "";
        String[] HTLC = {"", "Hundred", "Thousand", "Lakh", "Crore"}; //H-hundread , T-Thousand, ..
        int[] split = {0, 2, 3, 5, 7, 9};
        String[] temp = new String[split.length];
        boolean addzero = true;
        int len1 = number.length();
        if (len1 > split[split.length - 1]) {
            System.out.println("Error. Maximum Allowed digits " + split[split.length - 1]);
            System.exit(0);
        }
        for (int l = 1; l < split.length; l++)
            if (number.length() == split[l]) {
                addzero = false;
                break;
            }
        if (addzero) number = "0" + number;
        int len = number.length();
        int j = 0;
        //spliting & putting numbers in temp array.
        while (split[j] < len) {
            int beg = len - split[j + 1];
            int end = beg + split[j + 1] - split[j];
            temp[j] = number.substring(beg, end);
            j = j + 1;
        }

        for (int k = 0; k < j; k++) {
            twodigitword = ConvertOnesTwos(temp[k]);
            if (k >= 1) {
                if (twodigitword.trim().length() != 0) word = twodigitword + "" + HTLC[k] + " " + word;
            } else word = twodigitword;
        }
        return (word);
    }

    private static String ConvertOnesTwos(String t) {
        final String[] ones = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        final String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

        String word = "";
        int num = Integer.parseInt(t);
        if (num % 10 == 0) word = tens[num / 10] + " " + word;
        else if (num < 20) word = ones[num] + " " + word;
        else {
            word = tens[(num - (num % 10)) / 10] + word;
            word = word + " " + ones[num % 10];
        }
        return word;
    }

    public Object createStudent(MultipartHttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();

        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        try {
            FileStorageProperties fileStorageProperties = new FileStorageProperties();
            StudentRegister studentInfo = new StudentRegister();

            studentInfo.setOutlet(users.getOutlet());
            Long branchId = Long.valueOf(request.getParameter("branchId"));
            Branch branch = branchRepository.findByIdAndStatus(branchId, true);
            studentInfo.setBranch(branch);

            studentInfo.setFirstName(request.getParameter("firstName"));
            studentInfo.setMiddleName(request.getParameter("middleName"));
            studentInfo.setLastName(request.getParameter("lastName"));
            studentInfo.setGender(request.getParameter("gender"));
            if (request.getParameterMap().containsKey("dob")) {
                studentInfo.setBirthDate(LocalDate.parse(request.getParameter("dob")));
            }
            studentInfo.setAge(0L);
            if (request.getParameterMap().containsKey("age")) {
                studentInfo.setAge(Long.valueOf(request.getParameter("age")));
            }
            if (request.getParameterMap().containsKey("birthPlace")) {
                studentInfo.setBirthPlace(request.getParameter("birthPlace"));
            }
            if (request.getParameterMap().containsKey("nationalityId")) {
                studentInfo.setNationality(request.getParameter("nationalityId"));
            }

            if (request.getParameterMap().containsKey("motherTongueId")) {
                Long motherTongueId = Long.valueOf(request.getParameter("motherTongueId"));
                MotherTongue motherTongue = motherTongueRepository.findByIdAndStatus(motherTongueId, true);
                studentInfo.setMotherTongue(motherTongue);
            }
            if (request.getParameterMap().containsKey("religionId")) {
                Long religionId = Long.valueOf(request.getParameter("religionId"));
                Religion religion = religionRepository.findByIdAndStatus(religionId, true);
                studentInfo.setReligion(religion);
            }
            if (request.getParameterMap().containsKey("casteId")) {
                Long casteId = Long.valueOf(request.getParameter("casteId"));
                Caste caste = casteRepository.findByIdAndStatus(casteId, true);
                studentInfo.setCaste(caste);
            }
            if (request.getParameterMap().containsKey("subCasteId")) {
                Long subCasteId = Long.valueOf(request.getParameter("subCasteId"));
                SubCaste subCaste = subCasteRepository.findByIdAndStatus(subCasteId, true);
                studentInfo.setSubCaste(subCaste);
            }
            if (request.getParameterMap().containsKey("categoryId")) {
                Long categoryId = Long.valueOf(request.getParameter("categoryId"));
                CasteCategory casteCategory = categoryRepository.findByIdAndStatus(categoryId, true);
                studentInfo.setCasteCategory(casteCategory);
            }
            if (request.getParameterMap().containsKey("hometown")) {
                studentInfo.setHomeTown(request.getParameter("hometown"));
            }
            if (request.getParameterMap().containsKey("aadharNo")) {
                studentInfo.setAadharNo(request.getParameter("aadharNo"));
            }
            if (request.getParameterMap().containsKey("saralId")) {
                studentInfo.setSaralId(request.getParameter("saralId"));
            }

            studentInfo.setFatherName(request.getParameter("fatherName"));
            if (request.getParameterMap().containsKey("fatherOccupation")) {
                studentInfo.setFatherOccupation(request.getParameter("fatherOccupation"));
            }
            if (request.getParameterMap().containsKey("motherName")) {
                studentInfo.setMotherName(request.getParameter("motherName"));
            }
            if (request.getParameterMap().containsKey("motherOccupation")) {
                studentInfo.setMotherOccupation(request.getParameter("motherOccupation"));
            }
            if (request.getParameterMap().containsKey("officeAddress")) {
                studentInfo.setOfficeAddress(request.getParameter("officeAddress"));
            }
            if (request.getParameterMap().containsKey("currentAddress")) {
                studentInfo.setCurrentAddress(request.getParameter("currentAddress"));
            }
            if (request.getParameterMap().containsKey("sameAsCurrentAddress")) {
                studentInfo.setSameAsCurrentAddress(Boolean.valueOf(request.getParameter("sameAsCurrentAddress")));
            }
            if (request.getParameterMap().containsKey("permanentAddress")) {
                studentInfo.setPermanentAddress(request.getParameter("permanentAddress"));
            }
            if (request.getParameterMap().containsKey("phoneNoHome")) {
                studentInfo.setPhoneNoHome(Long.valueOf(request.getParameter("phoneNoHome")));
            }
            if (request.getParameterMap().containsKey("mobileNo")) {
                studentInfo.setMobileNo(Long.valueOf(request.getParameter("mobileNo")));
            }
            if (request.getParameterMap().containsKey("alternativeMobileNo")) {
                studentInfo.setAltMobileNo(Long.valueOf(request.getParameter("alternativeMobileNo")));
            }
            if (request.getParameterMap().containsKey("emailId")) {
                studentInfo.setEmailId(request.getParameter("emailId"));
            }
            if (request.getParameterMap().containsKey("generalRegisterNo")) {
                studentInfo.setGeneralRegisterNo(request.getParameter("generalRegisterNo"));
            }
            if (request.getParameterMap().containsKey("nameOfPrevSchool")) {
                studentInfo.setNameOfPreviousSchool(request.getParameter("nameOfPrevSchool"));
            }
            if (request.getParameterMap().containsKey("stdInPrevSchool")) {
                studentInfo.setStdInPreviousSchool(request.getParameter("stdInPrevSchool"));
            }
            if (request.getParameterMap().containsKey("result")) {
                studentInfo.setResult(request.getParameter("result"));
            }
            studentInfo.setStatus(true);

            if (request.getParameterMap().containsKey("doa")) {
                studentInfo.setDateOfAdmission(LocalDate.parse(request.getParameter("doa")));
            }

            if (request.getParameterMap().containsKey("studentIsOld")) {
                studentInfo.setTypeOfStudent(request.getParameter("studentIsOld"));
            }
            if (Integer.valueOf(request.getParameter("studentIsOld")) == 1) {
                Long standardId = Long.valueOf(request.getParameter("admittedStandardId"));
                Standard standard = standardRepository.findByIdAndStatus(standardId, true);
                studentInfo.setAdmittedStandard(standard);
                studentInfo.setTypeOfStudent(request.getParameter("studentIsOld"));
            }

            String lastStudentUniqueNo = numFormat.studentNumFormat(1);
            StudentRegister lastRecord = studentRegisterRepository.findTop1ByOrderByIdDesc();
            if (lastRecord != null) {
                int lastReceiptNum = Integer.parseInt(lastRecord.getStudentUniqueNo()) + 1;
                lastStudentUniqueNo = numFormat.studentNumFormat(lastReceiptNum);
            }
            studentInfo.setStudentUniqueNo(lastStudentUniqueNo);

            try {
                StudentRegister savedStudentInfo = studentRegisterRepository.save(studentInfo);

                boolean flag = false;
                if (savedStudentInfo != null) {
                    StudentAdmission studentAdmission1 = null;
                    if (request.getParameterMap().containsKey("academicYearId") && request.getParameterMap().containsKey("academicYearId")) {
                        LocalDate date = LocalDate.now();
                        StudentAdmission studentAdmission = new StudentAdmission();
                        studentAdmission.setStatus(true);
                        if (savedStudentInfo.getDateOfAdmission() != null) {
                            studentAdmission.setDateOfAdmission(savedStudentInfo.getDateOfAdmission());
                        } else {
                            studentAdmission.setDateOfAdmission(date);
                        }
                        studentAdmission.setOutlet(users.getOutlet());
                        studentAdmission.setBranch(savedStudentInfo.getBranch());
                        studentAdmission.setStudentRegister(savedStudentInfo);
                        studentAdmission.setCreatedAt(LocalDateTime.now());
                        studentAdmission.setCreatedBy(users.getId());

                        if (request.getParameterMap().containsKey("academicYearId")) {
                            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
                            AcademicYear academicYear = academicYearRepository.findByIdAndStatus(academicYearId, true);
                            studentAdmission.setAcademicYear(academicYear);
                        }

                        if (request.getParameterMap().containsKey("currentStandardId")) {
                            Long currentStandardId = Long.valueOf(request.getParameter("currentStandardId"));
                            Standard currentStandard = standardRepository.findByIdAndStatus(currentStandardId, true);
                            studentAdmission.setStandard(currentStandard);
                        }

                        if (request.getParameterMap().containsKey("divisionId")) {
                            Long divisionId = Long.valueOf(request.getParameter("divisionId"));
                            Division division = divisionRepository.findByIdAndStatus(divisionId, true);
                            studentAdmission.setDivision(division);
                        }

                        if (request.getParameterMap().containsKey("studentType")) {
                            studentAdmission.setStudentType(Integer.valueOf(request.getParameter("studentType")));
                        } else {
                            studentAdmission.setStudentType(null);
                        }
                        if (request.getParameterMap().containsKey("studentGroup")) {
                            studentAdmission.setStudentGroup(Integer.valueOf(request.getParameter("studentGroup")));
                        } else {
                            studentAdmission.setStudentGroup(null);
                        }

                        if (studentAdmission.getIsBusConcession() == Boolean.valueOf(request.getParameter("isBusConcession"))) {
                            flag = true;
                        }
                        if (studentAdmission.getIsBusConcession() == Boolean.valueOf(request.getParameter("isBusConcession"))) {
                            flag = true;
                        }

                        studentAdmission.setIsHostel(Boolean.valueOf(request.getParameter("isHostel")));
                        studentAdmission.setIsBusConcession(Boolean.valueOf(request.getParameter("isBusConcession")));

                        if (request.getParameterMap().containsKey("isVacation")) {

                            studentAdmission.setIsVacation(Boolean.valueOf(request.getParameter("isVacation")));
                        } else {
                            studentAdmission.setIsVacation(false);
                        }


                        studentAdmission.setBusConcessionAmount(0L);
                        if (request.getParameterMap().containsKey("busConcessionAmount")) {
                            studentAdmission.setBusConcessionAmount(Long.valueOf(request.getParameter("busConcessionAmount")));
                        }
                        if (request.getParameterMap().containsKey("nts")) {
                            studentAdmission.setNts(Boolean.valueOf(request.getParameter("nts")));
                        } else {
                            studentAdmission.setNts(false);
                        }
                        if (request.getParameterMap().containsKey("mts")) {

                            studentAdmission.setIsMts(Boolean.valueOf(request.getParameter("mts")));
                        } else {
                            studentAdmission.setIsMts(false);
                        }

                        if (request.getParameterMap().containsKey("foundation")) {
                            studentAdmission.setIsFoundation(Boolean.valueOf(request.getParameter("foundation")));
                        } else {
                            studentAdmission.setIsFoundation(false);
                        }
                        if (request.getParameterMap().containsKey("isScholarship")) {
                            studentAdmission.setIsScholarship(Boolean.valueOf(request.getParameter("isScholarship")));
                        } else {
                            studentAdmission.setIsScholarship(false);
                        }

                        if (request.getParameterMap().containsKey("concession")) {
                            studentAdmission.setConcessionAmount(Long.valueOf(request.getParameter("concession")));
                        }
                        studentAdmission.setIsRightOff(false);
                        studentAdmission1 = admissionRepository.save(studentAdmission);
                    }

                    try {
                        if (request.getFile("studentImage") != null) {
                            MultipartFile image = request.getFile("studentImage");
                            fileStorageProperties.setUploadDir("./uploads" + File.separator + savedStudentInfo.getId() + File.separator + "student" + File.separator);
                            String imagePath = fileStorageService.storeFile(image, fileStorageProperties);

                            if (imagePath != null) {
                                savedStudentInfo.setStudentImage("/uploads" + File.separator + savedStudentInfo.getId() + File.separator + "student" + File.separator + imagePath);
                            } else {
                                studentLogger.error("Failed to upload father image. Please try again! ");
                            }
                        }
                        if (request.getFile("fatherImage") != null) {
                            MultipartFile image = request.getFile("fatherImage");
                            fileStorageProperties.setUploadDir("./uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator);
                            String imagePath = fileStorageService.storeFile(image, fileStorageProperties);

                            if (imagePath != null) {
                                savedStudentInfo.setFatherImage("/uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator + imagePath);
                            } else {
                                studentLogger.error("Failed to upload father image. Please try again! ");
                            }
                        }
                        if (request.getFile("motherImage") != null) {
                            MultipartFile image = request.getFile("motherImage");
                            fileStorageProperties.setUploadDir("./uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator);
                            String imagePath = fileStorageService.storeFile(image, fileStorageProperties);

                            if (imagePath != null) {
                                savedStudentInfo.setMotherImage("/uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator + imagePath);
                            } else {
                                studentLogger.error("Failed to upload mother image. Please try again! ");
                            }
                        }
                        if (studentAdmission1 != null) {
                            FeesMaster feesMaster = null;
                            if (studentAdmission1.getStudentGroup() != null)
                                feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStudentGroupAndStatus(
                                        savedStudentInfo.getBranch().getId(), studentAdmission1.getStandard().getId(),
                                        studentAdmission1.getAcademicYear().getId(), studentAdmission1.getStudentType(),
                                        studentAdmission1.getStudentGroup(), true);
                            else
                                feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStatus(
                                        savedStudentInfo.getBranch().getId(), studentAdmission1.getStandard().getId(),
                                        studentAdmission1.getAcademicYear().getId(), studentAdmission1.getStudentType(), true);
                            if (feesMaster != null) {

                                FeesTransactionSummary feesTransactionSummary = new FeesTransactionSummary();
                                double outstanding = 0.0;
                                if (studentAdmission1.getStudentType() == 2 && (studentAdmission1.getStandard().getStandardName().equalsIgnoreCase("11") || studentAdmission1.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                    if (savedStudentInfo.getGender().equalsIgnoreCase("male")) {
                                        feesTransactionSummary.setBalance(feesMaster.getAmountForBoy());
                                        feesTransactionSummary.setTotalFees(feesMaster.getAmountForBoy());
                                        outstanding = feesMaster.getAmountForBoy();
                                    } else if (savedStudentInfo.getGender().equalsIgnoreCase("female")) {
                                        feesTransactionSummary.setBalance(feesMaster.getAmountForGirl());
                                        feesTransactionSummary.setTotalFees(feesMaster.getAmountForGirl());
                                        outstanding = feesMaster.getAmountForGirl();
                                    }
                                } else {
                                    feesTransactionSummary.setBalance(feesMaster.getAmount());
                                    feesTransactionSummary.setTotalFees(feesMaster.getAmount());
                                    outstanding = feesMaster.getAmount();
                                }

//                                outstanding = 71000
                                /******* getBusHeadFee ************/
//                                if(flag == true)
                                /*if (studentAdmission1.getIsBusConcession() == false) { // student bus not applicable
                                    outstanding = outstanding - headFee; // 71000 - 12000 = 59000
                                    double totalFees = outstanding;
                                    feesTransactionSummary.setTotalFees(totalFees);
                                    feesTransactionSummary.setBalance(outstanding);
                                }*/

                                /****** If student applicable bus yes/no then not deduct amount from fees master ******/
                                double headFee = getBusHeadFee(savedStudentInfo, feesMaster); // 12000
                                outstanding = outstanding - headFee; // 71000 - 12000 = 59000
                                double totalFees = outstanding;
                                feesTransactionSummary.setTotalFees(totalFees);
                                feesTransactionSummary.setBalance(outstanding);

                                /**** Is student applicablevacation yes th en consider amount otherwise deduct amount from fees master ******/
                                double vacationHeadFee = getVacationHeadFee(savedStudentInfo, feesMaster); // 12000
                                if (!studentAdmission1.getIsVacation()) { // student bus not applicable
                                    outstanding = outstanding - vacationHeadFee; // 71000 - 12000 = 59000
                                    totalFees = outstanding;
                                    feesTransactionSummary.setTotalFees(totalFees);
                                    feesTransactionSummary.setBalance(outstanding);
                                }

                                /**** Is student applicable Scholarship yes then consider amount otherwise deduct amount from fees Master ****/

                                double scholarshipfee = getScholarshipFee(savedStudentInfo, feesMaster); //5500
                                if (!studentAdmission1.getIsScholarship()) {

                                    outstanding = outstanding - scholarshipfee;
                                    totalFees = outstanding;
                                    feesTransactionSummary.setTotalFees(totalFees);
                                    feesTransactionSummary.setBalance(outstanding);

                                }
                                double foundationfee = getFoundationFee(savedStudentInfo, feesMaster);
                                if (!studentAdmission1.getIsFoundation()) {
                                    outstanding = outstanding - foundationfee;
                                    totalFees = outstanding;
                                    feesTransactionSummary.setTotalFees(totalFees);
                                    feesTransactionSummary.setBalance(outstanding);
                                }

                                double ntsFee = getNtsFee(savedStudentInfo, feesMaster);
                                if (!studentAdmission1.getNts()) {
                                    outstanding = outstanding - ntsFee;
                                    totalFees = outstanding;
                                    feesTransactionSummary.setTotalFees(totalFees);
                                    feesTransactionSummary.setBalance(outstanding);
                                }

                                double mtsFee = getMtsFee(savedStudentInfo, feesMaster);
                                if (!studentAdmission1.getIsMts()) {
                                    outstanding = outstanding - mtsFee;
                                    totalFees = outstanding;
                                    feesTransactionSummary.setTotalFees(totalFees);
                                    feesTransactionSummary.setBalance(outstanding);
                                }


                                feesTransactionSummary.setPaidAmount(0.0);
                                feesTransactionSummary.setStudentRegister(savedStudentInfo);
                                feesTransactionSummary.setStandard(studentAdmission1.getStandard());
                                feesTransactionSummary.setDivision(studentAdmission1.getDivision());
                                feesTransactionSummary.setFeesMaster(feesMaster);
                                feesTransactionSummary.setAcademicYear(studentAdmission1.getAcademicYear());
                                feesTransactionSummary.setStudentType(studentAdmission1.getStudentType());
                                feesTransactionSummary.setBranch(studentAdmission1.getBranch());
                                feesTransactionSummary.setOutlet(studentAdmission1.getOutlet());
                                feesTransactionSummary.setStudentGroup(studentAdmission1.getStudentGroup());
                                feesTransactionSummary.setCreatedBy(users.getId());
                                feesTransactionSummary.setStatus(true);

                                feesTransactionSummaryRepository.save(feesTransactionSummary);

                                LedgerMaster ledgerMaster = createLedgerForStudent(studentInfo, users);
                                /******** Get Hostel fee by searching hostel name ********/
//                                double hostelFee = getHostelHeadFee(savedStudentInfo, feesMaster);

                                /******** Get Hostel fee by get hostel receipt under heads amount ********/
                                double hostelFee = getHostelHeadFees(savedStudentInfo, feesMaster);
                                double totalFeeExceptHostel = outstanding - hostelFee;

                                createTranxSalesInvoice(studentInfo, users, ledgerMaster.getId(), outstanding, feesMaster, totalFeeExceptHostel, studentAdmission1);
                                if (hostelFee > 0) {
                                    Branch hostelBranch = branchRepository.getHostelBranchRecord(users.getOutlet().getId(), true);
                                    if (hostelBranch != null) {
                                        LedgerMaster hostelStudentLedger = createLedgerForStudentForHostel(hostelBranch, studentInfo, users);
                                        if (hostelStudentLedger == null) {
                                            System.out.println("ledger not created >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                            responseMessage.setMessage("Failed to create ledger in hostel, please delete & add once again");
                                            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                                            return responseMessage;
                                        }

                                        createTranxSalesInvoiceForHostel(hostelBranch, studentInfo, users, hostelStudentLedger.getId(), hostelFee, feesMaster, studentAdmission1, 0.0);
                                    } else {
                                        System.out.println("Hostel branch not exists");
                                    }
                                }
                            }
                        }
//                        studentRegisterRepository.save(savedStudentInfo);
                        responseMessage.setMessage("Student registered successfully");
                        responseMessage.setResponseStatus(HttpStatus.OK.value());
                    } catch (Exception e) {
                        studentLogger.error("saving student images exception " + e.getMessage());
                        System.out.println("saving student images Exception " + e.getMessage());
                        e.printStackTrace();
                        responseMessage.setMessage("Failed to register student");
                        responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                }
            } catch (Exception e) {
                studentLogger.error("saving student exception " + e.getMessage());
                System.out.println("Exception " + e.getMessage());
                e.printStackTrace();
                responseMessage.setMessage("Failed to register student");
                responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

        } catch (Exception e) {
            e.printStackTrace();
            studentLogger.error("saving student exception " + e.getMessage());
            System.out.println("Exception " + e.getMessage());
            //  e.printStackTrace(feas);
            responseMessage.setMessage("Failed to register student");
            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }


    private void createTranxSalesInvoiceForHostel(Branch hostelBranch, StudentRegister studentInfo, Users users, Long ledgerId,
                                                  double hostelFee, FeesMaster feesMaster, StudentAdmission studentAdmission1, Double outstandingBal) {
        TranxSalesInvoice mSalesTranx = null;
        TransactionTypeMaster tranxType = transactionTypeMasterRepository.findByTransactionCodeIgnoreCase("feestr");
        /* save into sales invoices  */
        Outlet outlet = users.getOutlet();
        TranxSalesInvoice invoiceTranx = new TranxSalesInvoice();
        invoiceTranx.setBranch(hostelBranch);
        invoiceTranx.setOutlet(outlet);
        LocalDate date = LocalDate.now();
        invoiceTranx.setBillDate(studentAdmission1.getDateOfAdmission() != null ? studentAdmission1.getDateOfAdmission() : date);
        /* fiscal year mapping */
//        FiscalYear fiscalYear = generateFiscalYear.getFiscalYear(date);
        FiscalYear fiscalYear = generateFiscalYear.getFiscalYear(studentAdmission1.getDateOfAdmission());
        if (fiscalYear != null) {
            invoiceTranx.setFiscalYear(fiscalYear);
            invoiceTranx.setFinancialYear(fiscalYear.getFiscalYear());
        }
        /* End of fiscal year mapping */

        Long count = tranxSalesInvoiceRepository.findLastRecord(users.getOutlet().getId(), hostelBranch.getId());
        String serailNo = String.format("%05d", count + 1);
        GenerateDates generateDates = new GenerateDates();
        String currentMonth = generateDates.getCurrentMonth().substring(0, 3);
        String siCode = "SI" + currentMonth + serailNo;
        invoiceTranx.setSalesSerialNumber(Long.parseLong(serailNo));
        invoiceTranx.setSalesInvoiceNo(siCode);
//        LedgerMaster feesAc = ledgerMasterRepository.findByLedgerNameIgnoreCaseAndOutletIdAndBranchIdAndStatus(
//        "Fees A/c", users.getOutlet().getId(), studentInfo.getBranch().getId(), true);
        AssociateGroups feesAc = associateGroupsRepository.findByAssociatesNameIgnoreCaseAndOutletIdAndBranchIdAndStatus("Fees Account", users.getOutlet().getId(), hostelBranch.getId(), true);
        LedgerMaster sundryDebtors = ledgerMasterRepository.findByIdAndStatus(ledgerId, true);
        invoiceTranx.setFeesAct(feesAc);
        invoiceTranx.setSundryDebtors(sundryDebtors);
        if (outstandingBal == 0) {
            invoiceTranx.setTotalBaseAmount(hostelFee);
            invoiceTranx.setTotalAmount(hostelFee);
            invoiceTranx.setBalance(hostelFee);
        } else {
            invoiceTranx.setTotalBaseAmount(outstandingBal);
            invoiceTranx.setTotalAmount(outstandingBal);
            invoiceTranx.setBalance(outstandingBal);
        }
        String studName = studentInfo.getFirstName();
        if (studentInfo.getFatherName() != null) studName = studName + " " + studentInfo.getFatherName();
        if (studentInfo.getLastName() != null) studName = studName + " " + studentInfo.getLastName();
        invoiceTranx.setNarration(studName + " registered and student unique number is " + studentInfo.getStudentUniqueNo());
        invoiceTranx.setCreatedBy(users.getId());
        invoiceTranx.setStatus(true);
        invoiceTranx.setOperations("inserted");

        try {
            mSalesTranx = tranxSalesInvoiceRepository.save(invoiceTranx);
            /* Save into Sales Duties and Taxes */
        } catch (DataIntegrityViolationException e) {
            System.out.println("Exception:" + e.getMessage());
            // throw new Exception(e.getMessage());
        } catch (Exception e1) {
            System.out.println("Exception:" + e1.getMessage());
            // throw new Exception(e1.getMessage());
        }

        if (mSalesTranx != null) {
            LedgerCommonPostings ledgerCommonPostings = new LedgerCommonPostings();
            /** Accounting Postings  **/
            insertIntoTranxDetailSD(mSalesTranx, tranxType, "create"); //for Sundry Debtors or Students : dr
          /*  ledgerCommonPostings.callToPostings(
                    mSalesTranx.getTotalAmount(),
                    mSalesTranx.getSundryDebtors(),tranxType,mSalesTranx.getSundryDebtors().getAssociateGroups(),mSalesTranx.getFiscalYear(),
                    mSalesTranx.getBranch(),mSalesTranx.getOutlet(),mSalesTranx.getBillDate(),mSalesTranx.getId(),mSalesTranx.getSalesInvoiceNo(),
                    "DR",true,"Sales Invoice","create");*/
            //  insertIntoTranxDetailFA(mSalesTranx, tranxType, feesMaster); // for Fees Accounts : cr
            try {
                hostelFeeLedgerPosting(studentInfo, feesMaster, mSalesTranx, tranxType, hostelBranch, "create");
            } catch (Exception ex) {
                ex.printStackTrace();
                studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
            }
        }
    }

    private LedgerMaster createLedgerForStudentForHostel(Branch hostelBranch, StudentRegister studentRegister, Users users) {
        LedgerMaster ledgerMaster = new LedgerMaster();
        PrincipleGroups groups = null;
        Principles principles = null;
        Foundations foundations = null;
        LedgerMaster mLedger = null;
        principles = principleRepository.findByIdAndStatus(1, true);
        foundations = principles.getFoundations();

        groups = principleGroupsRepository.findByIdAndStatus(1, true);
        if (groups != null) {
            ledgerMaster.setPrincipleGroups(groups);
            ledgerMaster.setPrinciples(principles);
            ledgerMaster.setUniqueCode(groups.getUniqueCode());
        } else {
            ledgerMaster.setPrinciples(principles);
            ledgerMaster.setUniqueCode(principles.getUniqueCode());
        }
        if (foundations != null) {
            ledgerMaster.setFoundations(foundations);
        }
        ledgerMaster.setIsPrivate(false);
        ledgerMaster.setIsDeleted(false); //isDelete : true means , we can delete this ledger
        ledgerMaster.setStatus(true);
        ledgerMaster.setIsDefaultLedger(false);

        ledgerMaster.setBranch(hostelBranch);
        ledgerMaster.setOutlet(users.getOutlet());
        ledgerMaster.setCreatedBy(users.getId());

        String studName = studentRegister.getFirstName();
        if (studentRegister.getFatherName() != null) studName = studName + " " + studentRegister.getFatherName();
        if (studentRegister.getLastName() != null) studName = studName + " " + studentRegister.getLastName();
        ledgerMaster.setLedgerName(studName);
        ledgerMaster.setSlugName("sundry_debtors");
        ledgerMaster.setStudentRegister(studentRegister);
        ledgerMaster.setUnderPrefix("PG#1");

        ledgerMaster.setTaxType("NA");
        ledgerMaster.setMailingName(studName);
        ledgerMaster.setOpeningBalType("Dr");

        if (studentRegister.getCurrentAddress() != null) {
            ledgerMaster.setAddress(studentRegister.getCurrentAddress());
        }
        ledgerMaster.setPincode(0L);
        if (studentRegister.getEmailId() != null) {
            ledgerMaster.setEmail(studentRegister.getEmailId());
        } else {
            ledgerMaster.setEmail("NA");
        }
        ledgerMaster.setMobile(studentRegister.getMobileNo());
        ledgerMaster.setTaxable(false);
        ledgerMaster.setBankName("NA");
        ledgerMaster.setAccountNumber("NA");
        ledgerMaster.setIfsc("NA");
        ledgerMaster.setBankBranch("NA");
        ledgerMaster.setOpeningBal(0.0);

        /* pune demo visit changes */
        ledgerMaster.setCreditDays(0);
        ledgerMaster.setApplicableFrom("NA");
        ledgerMaster.setFoodLicenseNo("NA");

        LedgerMaster ledgerMaster1 = ledgerMasterRepository.save(ledgerMaster);
        return ledgerMaster1;
    }

    /****** need to validate : Risk ******/
    public void hostelFeeLedgerPosting(StudentRegister studentInfo, FeesMaster feesMaster, TranxSalesInvoice mSalesTranx, TransactionTypeMaster tranxType, Branch hostelBranch, String key) {
//        String feesDetails = feesDetailsRepository.checkHostelFeeHeadData(feesMaster.getId());
//        if (feesDetails != null) {
//            String[] details = feesDetails.split(",");
//
//            FeeHead feeHead = feeHeadRepository.findByIdAndStatus(Long.parseLong(details[3]), true);
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatusAndHostelsOnly(feesMaster.getId(), false, true);
        for (FeesDetails feesDetails : feesDetailsList) {
            double fees = 0;
            if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentInfo.getGender().equalsIgnoreCase("male")) {
                fees = feesDetails.getAmountForBoy();

            } else if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentInfo.getGender().equalsIgnoreCase("female")) {
                fees = feesDetails.getAmountForGirl();
            } else {
                fees = feesDetails.getAmount();

            }
//            ledgerTransactionDetailsRepository.insertIntoLegerTranxDetail(
            /*ledgerTransactionDetailsRepository.insertIntoLegerTranxDetailWithTranxDate(
                    mSalesTranx.getFeesAct().getFoundations().getId(), mSalesTranx.getFeesAct().getPrinciples().getId(),
                    mSalesTranx.getFeesAct().getPrincipleGroups() != null ? mSalesTranx.getFeesAct().getPrincipleGroups().getId() : null,
                    null, tranxType.getId(), null, hostelBranch.getId(), mSalesTranx.getOutlet().getId(), "pending",
                    0.0, fees, mSalesTranx.getBillDate() != null ? mSalesTranx.getBillDate() : null, null,
                    mSalesTranx.getId(), tranxType.getTransactionName() != null ? tranxType.getTransactionName() : null,
                    mSalesTranx.getFeesAct() != null ? mSalesTranx.getFeesAct().getUnder_prefix() : null,
                    mSalesTranx.getFinancialYear() != null ? mSalesTranx.getFinancialYear() : null,
                    mSalesTranx.getCreatedBy(), feesDetails.getFeeHead().getLedger().getId(),
                    mSalesTranx.getSalesInvoiceNo() != null ? mSalesTranx.getSalesInvoiceNo() : null, true);*/
            ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType, null, mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);

            /******* Insert into Sales Invoice Details against Fees Head *****/
            TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
            invoiceDetails.setAmount(fees);
            if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
            invoiceDetails.setOutlet(mSalesTranx.getOutlet());
            if (mSalesTranx.getFiscalYear() != null) invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
            invoiceDetails.setTranxSalesInvoice(mSalesTranx);
            invoiceDetails.setFeeHead(feesDetails.getFeeHead());
            invoiceDetails.setStatus(true);
            try {
                tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
            } catch (Exception ex) {
                ex.printStackTrace();
                studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
            }
        }
    }

    private void createTranxSalesInvoice(StudentRegister studentInfo, Users users, Long ledgerId, double outstanding, FeesMaster feesMaster, double totalExceptHostel, StudentAdmission studentAdmission1) throws Exception {
        TranxSalesInvoice mSalesTranx = null;
        TransactionTypeMaster tranxType = transactionTypeMasterRepository.findByTransactionCodeIgnoreCase("feestr");
        /* save into sales invoices  */
        Branch branch = null;
        if (users.getBranch() != null) branch = users.getBranch();
        branch = users.getBranch();
        Outlet outlet = users.getOutlet();
        TranxSalesInvoice invoiceTranx = new TranxSalesInvoice();
        invoiceTranx.setBranch(branch);
        invoiceTranx.setOutlet(outlet);
        LocalDate date = LocalDate.now();
//        invoiceTranx.setBillDate(studentAdmission1.getDateOfAdmission() != null ? studentAdmission1.getDateOfAdmission() : date);
        invoiceTranx.setBillDate(date);

        /* fiscal year mapping */
        FiscalYear fiscalYear = generateFiscalYear.getFiscalYear(studentAdmission1.getDateOfAdmission());
        if (fiscalYear != null) {
            invoiceTranx.setFiscalYear(fiscalYear);
            invoiceTranx.setFinancialYear(fiscalYear.getFiscalYear());
        }
        /* End of fiscal year mapping */

        Long count = tranxSalesInvoiceRepository.findLastRecord(users.getOutlet().getId(), branch.getId());
        String serailNo = String.format("%05d", count + 1);
        GenerateDates generateDates = new GenerateDates();
        String currentMonth = generateDates.getCurrentMonth().substring(0, 3);
        String siCode = "SI" + currentMonth + serailNo;
        invoiceTranx.setSalesSerialNumber(Long.parseLong(serailNo));
        invoiceTranx.setSalesInvoiceNo(siCode);
        //        LedgerMaster feesAc = ledgerMasterRepository.findByLedgerNameIgnoreCaseAndOutletIdAndBranchIdAndStatus("Fees A/c", users.getOutlet().getId(), studentInfo.getBranch().getId(), true);
        AssociateGroups feesAc = associateGroupsRepository.findByAssociatesNameIgnoreCaseAndOutletIdAndBranchIdAndStatus("Fees Account", users.getOutlet().getId(), studentInfo.getBranch().getId(), true);
        LedgerMaster sundryDebtors = ledgerMasterRepository.findByIdAndStatus(ledgerId, true);
        invoiceTranx.setFeesAct(feesAc);
        invoiceTranx.setSundryDebtors(sundryDebtors);
        invoiceTranx.setTotalBaseAmount(totalExceptHostel);
        invoiceTranx.setTotalAmount(totalExceptHostel);
        invoiceTranx.setBalance(totalExceptHostel);

        String studName = studentInfo.getFirstName();
        if (studentInfo.getFatherName() != null) studName = studName + " " + studentInfo.getFatherName();
        if (studentInfo.getLastName() != null) studName = studName + " " + studentInfo.getLastName();
        invoiceTranx.setNarration(studName + " registered and student unique number is " + studentInfo.getStudentUniqueNo());
        invoiceTranx.setCreatedBy(users.getId());
        invoiceTranx.setStatus(true);
        invoiceTranx.setOperations("inserted");

        try {
            mSalesTranx = tranxSalesInvoiceRepository.save(invoiceTranx);
            /* Save into Sales Duties and Taxes */
        } catch (DataIntegrityViolationException e) {
            System.out.println("Exception:" + e.getMessage());
            // throw new Exception(e.getMessage());
        } catch (Exception e1) {
            System.out.println("Exception:" + e1.getMessage());
            // throw new Exception(e1.getMessage());
        }
        if (mSalesTranx != null) {
            /** Accounting Postings  **/
            insertIntoTranxDetailSD(mSalesTranx, tranxType, "create"); //for Sundry Debtors or Students : dr
            insertIntoTranxDetailFA(studentInfo, mSalesTranx, tranxType, feesMaster, "create"); // for Fees Accounts : cr
        }
    }

    public JsonObject getStudentListForTransaction(MultipartHttpServletRequest request) {
        JsonObject responseMessage = new JsonObject();
        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            Long branchId = Long.valueOf(request.getParameter("branchId"));
            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
            Long standardId = Long.valueOf(request.getParameter("standardId"));
            Long divisionId = Long.valueOf(request.getParameter("divisionId"));
            Integer studentType = Integer.valueOf(request.getParameter("studentType"));
            List<StudentAdmission> studentInfos = admissionRepository.findByOutletIdAndBranchIdAndAcademicYearIdAndStandardIdAndDivisionIdAndStudentTypeAndStatus(users.getOutlet().getId(), branchId, academicYearId, standardId, divisionId, studentType, true);
            JsonArray jsonArray = new JsonArray();

            for (StudentAdmission studentInfo : studentInfos) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", studentInfo.getStudentRegister().getId());
                jsonObject.addProperty("firstName", studentInfo.getStudentRegister().getFirstName());
                jsonObject.addProperty("lastName", studentInfo.getStudentRegister().getLastName());
                jsonArray.add(jsonObject);
            }
            responseMessage.add("responseObject", jsonArray);
            responseMessage.addProperty("responseStatus", HttpStatus.OK.value());
        } catch (Exception e) {
            studentLogger.error("getStudentListForTransaction " + e.getMessage());
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();

            responseMessage.addProperty("message", "Failed to load data");
            responseMessage.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }

    public JsonObject getStudentDetailsForBonafide(MultipartHttpServletRequest request) {

        JsonObject result = new JsonObject();
        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            Long branchId = Long.valueOf(request.getParameter("branchId"));
            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
            Long standardId = Long.valueOf(request.getParameter("standardId"));
            Long divisionId = Long.valueOf(request.getParameter("divisionId"));
            Integer studentType = Integer.valueOf(request.getParameter("studentType"));
            Long studentRollNo = Long.valueOf(request.getParameter("studentId"));
            StudentAdmission studentAdmission = studentAdmissionRepository.findByOutletIdAndBranchIdAndAcademicYearIdAndStandardIdAndDivisionIdAndStudentRegisterIdAndStudentTypeAndStatus(users.getOutlet().getId(), branchId, academicYearId, standardId, divisionId, studentRollNo, studentType, true);
//            StudentRegister sregister = studentRegisterRepository.findByOutletIdAndBranchIdAndAcademicYearIdAndCurrentStandardIdAndDivisionIdAndStudentTypeAndIdAndStatus(
//                    users.getOutlet().getId(), branchId, academicYearId, standardId, divisionId, studentType, studentRollNo, true);
            if (studentAdmission != null) {
                JsonObject jobject = new JsonObject();
                jobject.addProperty("studentName", studentAdmission.getStudentRegister().getFirstName() + " " + studentAdmission.getStudentRegister().getMiddleName() + " " + studentAdmission.getStudentRegister().getLastName());
                jobject.addProperty("fatherName", studentAdmission.getStudentRegister().getFatherName());
                jobject.addProperty("motherName", studentAdmission.getStudentRegister().getMotherName());
                if (studentAdmission.getStudentRegister().getMotherTongue() != null) {
                    jobject.addProperty("motherTongue", studentAdmission.getStudentRegister().getMotherTongue().getName());
                } else {
                    jobject.addProperty("motherTongue", "");
                }
                jobject.addProperty("gender", studentAdmission.getStudentRegister().getGender());

                if (studentAdmission.getStudentRegister().getGeneralConduct() != null) {
                    jobject.addProperty("generalconduct", studentAdmission.getStudentRegister().getGeneralConduct());
                } else {
                    jobject.addProperty("generalconduct", "");
                }
                if (studentAdmission.getStudentRegister().getReasonOfLeaveSchool() != null) {
                    jobject.addProperty("reasonofleavingschool", studentAdmission.getStudentRegister().getReasonOfLeaveSchool());
                } else {
                    jobject.addProperty("reasonofleavingschool", "");
                }
                if (studentAdmission.getStudentRegister().getDol() != null) {
                    jobject.addProperty("dol", studentAdmission.getStudentRegister().getDol().toString());
                } else {
                    jobject.addProperty("dol", "");
                }

                jobject.addProperty("religion", studentAdmission.getStudentRegister().getReligion().getReligionName());
                if (studentAdmission.getStudentRegister().getSubCaste() != null) {

                    jobject.addProperty("subcaste", studentAdmission.getStudentRegister().getSubCaste().getSubCasteName());
                } else {
                    jobject.addProperty("subcaste", "");

                }
                if (studentAdmission.getStudentRegister().getDateOfAdmission() != null) {
                    jobject.addProperty("doa", studentAdmission.getStudentRegister().getDateOfAdmission().toString());
                } else {
                    jobject.addProperty("doa", "");
                }
                jobject.addProperty("grNo", studentAdmission.getStudentRegister().getGeneralRegisterNo());

                jobject.addProperty("affiliationNO", studentAdmission.getBranch().getAffiliationNo());
                jobject.addProperty("udiseNo", studentAdmission.getBranch().getUdiseNo());
                jobject.addProperty("studentId", studentAdmission.getId());
                jobject.addProperty("divisionName", studentAdmission.getDivision().getDivisionName());
                jobject.addProperty("academicYear", studentAdmission.getAcademicYear().getYear());
                jobject.addProperty("placeofbirth", studentAdmission.getStudentRegister().getBirthPlace());
                if (studentAdmission.getStudentRegister().getBirthDate() != null) {
                    String[] dobString = studentAdmission.getStudentRegister().getBirthDate().toString().split("-");

                    String year = NumberToWord(dobString[0]);
                    String month = studentAdmission.getStudentRegister().getBirthDate().getMonth().toString();
                    String day = NumberToWord(dobString[2]);

                    jobject.addProperty("dob", studentAdmission.getStudentRegister().getBirthDate().toString());
                    jobject.addProperty("dob1", day.toUpperCase() + " " + month + " " + year.toUpperCase());
                } else {
                    jobject.addProperty("dob", "");
                    jobject.addProperty("dob1", "");
                }

//                jobject.addProperty("grNo", studentAdmission.getBranch().getGrNo());
                jobject.addProperty("gender", studentAdmission.getStudentRegister().getGender());
                if (studentAdmission.getStudentRegister().getCaste() != null) {

                    jobject.addProperty("caste", studentAdmission.getStudentRegister().getCaste().getCasteName());
                } else {
                    jobject.addProperty("caste", "");
                }
//                jobject.addProperty("dob",studentAdmission.getBirthDate());
                jobject.addProperty("standardId", studentAdmission.getStandard().getId());
                jobject.addProperty("standardName", studentAdmission.getStandard().getStandardName());
                jobject.addProperty("divisionId", studentAdmission.getDivision().getId());
                result.add("responseObject", jobject);
                result.addProperty("responseStatus", HttpStatus.OK.value());


            } else {
                result.addProperty("message", "Data not found");
                result.addProperty("responseStatus", HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            e.printStackTrace();
            System.out.println("Exception" + e.getMessage());
        }
        return result;

    }

    public JsonObject getStudentList(HttpServletRequest request) {
        JsonArray result = new JsonArray();
        JsonObject res = new JsonObject();
        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            Long outletId = users.getOutlet().getId();
            List<Object[]> list = new ArrayList<>();
            if (users.getBranch() != null) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndStatus(outletId, users.getBranch().getId(), true);
            } else {
                list = studentRegisterRepository.getStudentsByOutletIdAndStatus(outletId, true);
            }
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = list.get(i);

                StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(Long.parseLong(obj[0].toString()), true);
                StudentAdmission studentAdmission = studentAdmissionRepository.findTop1ByStudentRegisterIdOrderByIdDesc(studentRegister.getId());
                JsonObject response = new JsonObject();
                response.addProperty("id", studentRegister.getId());
                response.addProperty("firstName", studentRegister.getFirstName());
                response.addProperty("lastName", studentRegister.getLastName());
                response.addProperty("birthDate", studentRegister.getBirthDate() != null ? studentRegister.getBirthDate().toString() : "");
                response.addProperty("outletName", studentRegister.getOutlet().getCompanyName());
                response.addProperty("branchName", studentRegister.getBranch().getBranchName());
                response.addProperty("academicYear", studentAdmission != null ? studentAdmission.getAcademicYear().getYear() : "");
                response.addProperty("standardName", studentAdmission != null ? studentAdmission.getStandard().getStandardName() : "");
                response.addProperty("dateofAdmission", studentRegister.getDateOfAdmission().toString());
//                response.addProperty("divisionName", sinfo.getDivision() != null ? sinfo.getDivision().getDivisionName() : "");
                if (studentAdmission.getStudentType() == 1) {
                    response.addProperty("studentType", "Day Scholar");
                } else if (studentAdmission.getStudentType() == 2) {
                    response.addProperty("studentType", "Residential");
                }
                response.addProperty("mobileNo", studentRegister.getMobileNo());
                response.addProperty("currentAddress", studentRegister.getCurrentAddress());
                result.add(response);
            }
            res.add("responseObject", result);
            res.addProperty("responseStatus", HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            studentLogger.error("getStudentInfo -> failed to load data " + e.getMessage());
            res.addProperty("message", "Failed to load data");
            res.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }


    public JsonObject getStudentListByStandard(HttpServletRequest request) {
        JsonObject result = new JsonObject();
        JsonArray res = new JsonArray();
        Map<String, String[]> paramMap = request.getParameterMap();
        Long standardId = 0L;
        Integer studentType = 0;
        Long academicYearId = 0L;
        Long busStopId = 0L;
        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));

            Long branchId = users.getBranch().getId();
            if (paramMap.containsKey("standardId")) standardId = Long.valueOf(request.getParameter("standardId"));
            if (paramMap.containsKey("busStopId")) busStopId = Long.valueOf(request.getParameter("busStopId"));
            if (paramMap.containsKey("studentType")) studentType = Integer.valueOf(request.getParameter("studentType"));
            if (paramMap.containsKey("academicYearId"))
                academicYearId = Long.valueOf(request.getParameter("academicYearId"));
            Long id = users.getOutlet().getId();

            String query2 = "SELECT student_register_tbl.id, IFNULL(first_name, ''), IFNULL(last_name, ''), IFNULL(gender, '')," +
                    " IFNULL(mobile_no, ''), IFNULL(birth_date, ''),  IFNULL(student_register_tbl.date_of_admission, ''), IFNULL(father_name, '') ," +
                    " IFNULL(mother_name,''),IFNULL(permanent_address,''), IFNULL(student_register_tbl.type_of_student,''), IFNULL(caste_tbl.caste_name,'')," +
                    " IFNULL(sub_caste_tbl.sub_caste_name, ''), IFNULL(caste_category_tbl.category_name, ''),IFNULL(aadhar_no,'') ,IFNULL(student_unique_no,'')  " +
                    " FROM student_register_tbl LEFT JOIN caste_tbl ON student_register_tbl.caste_id=caste_tbl.id LEFT JOIN sub_caste_tbl ON" +
                    " student_register_tbl.sub_caste_id=sub_caste_tbl.id LEFT JOIN caste_category_tbl ON student_register_tbl.caste_category_id=caste_category_tbl.id " +
                    "  WHERE student_register_tbl.outlet_id=" + users.getOutlet().getId() + " AND student_register_tbl.branch_id=" + branchId + " AND student_register_tbl.status=1 ";

            query2 += " ORDER BY TRIM(student_register_tbl.last_name)";
            System.out.println("query2 " + query2);
            Query q2 = entityManager.createNativeQuery(query2);
            List<Object[]> list = q2.getResultList();
            System.out.println("list.size() " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = list.get(i);

                JsonObject response = new JsonObject();
                response.addProperty("TypeofStudent", "");
                response.addProperty("studentType", "");
                response.addProperty("busStopName", "");
                response.addProperty("busStopFee", "");
                response.addProperty("academicYear", "");
                response.addProperty("standardName", "");

                System.out.println("studentId =>" + obj[0].toString());
//                StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(Long.parseLong(obj[0].toString()), true);

                StudentAdmission studAdmissionData = studentAdmissionRepository.getStudentLastAcademicData(Long.parseLong(obj[0].toString()));
//                    System.out.println("studAdmissionData" + studAdmissionData);
//
                if (obj[10] != null) {
                    if (!obj[10].toString().equalsIgnoreCase("") && Integer.parseInt(obj[10].toString()) == 1) {
                        response.addProperty("TypeofStudent", "New");
                    } else if (!obj[10].toString().equalsIgnoreCase("") && Integer.parseInt(obj[10].toString()) == 2) {
                        response.addProperty("TypeofStudent", "Old");
                    }
                }

                if (studAdmissionData != null) {
                    if (studAdmissionData.getStudentType() != null && studAdmissionData.getStudentType() == 1) {
                        response.addProperty("studentType", "Day Scholar");
                    } else if (studAdmissionData.getStudentType() != null && studAdmissionData.getStudentType() == 2) {
                        response.addProperty("studentType", "Residential");
                    }
                    response.addProperty("academicYear", studAdmissionData.getAcademicYear().getYear());
                    response.addProperty("standardName", studAdmissionData.getStandard().getStandardName());
                    response.addProperty("dateOfAdmission", studAdmissionData.getDateOfAdmission() != null ?
                            studAdmissionData.getDateOfAdmission().toString() : "");
                }

                String busData = studentRegisterRepository.getStudentBusData(obj[0].toString(), 1L);
                System.out.println("busData ===>" + busData);
                if (busData != null) {
                    String[] studBusData = busData.split(",");
                    if (!studBusData[0].equalsIgnoreCase("")) {
                        response.addProperty("busStopName", studBusData[0]);
                    }
                    if (!studBusData[1].equalsIgnoreCase("")) {
                        response.addProperty("busStopFee", studBusData[1]);
                    }
                }

                String studentName = obj[2].toString();
                String fatherName = obj[7].toString() != null ? obj[7].toString() : " ";
                if (!fatherName.equalsIgnoreCase("")) studentName = studentName + " " + fatherName;
                studentName = studentName + " " + obj[1].toString();
                System.out.println("studentName =====>>>>>>>>>>>" + studentName);

                response.addProperty("fatherName", fatherName);
                response.addProperty("motherName", obj[8].toString() != null ? obj[8].toString() : "");
                response.addProperty("address", obj[9].toString() != null ? obj[9].toString() : "");
                response.addProperty("casteName", obj[11].toString() != null ? obj[11].toString() : "");
                response.addProperty("subCasteName", obj[12].toString() != null ? obj[12].toString() : "");
                response.addProperty("category", obj[13].toString() != null ? obj[13].toString() : "");
                response.addProperty("aadharNo", obj[14].toString() != null ? obj[14].toString() : "");
                response.addProperty("studentId", obj[15].toString() != null ? obj[15].toString() : "");
                response.addProperty("studentName", studentName);
                response.addProperty("studentAdmissionId", studAdmissionData != null ?
                        studAdmissionData.getId() : 0);

                response.addProperty("dateOfAdmission", "");
                if (!obj[6].toString().equalsIgnoreCase("")) {
                    response.addProperty("dateOfAdmission", obj[6].toString());
                }


                System.out.println("Student Register Id" + obj[0].toString());
                response.addProperty("id", obj[0].toString());
                response.addProperty("firstName", obj[1].toString());
                response.addProperty("lastName", obj[2].toString());
                response.addProperty("gender", obj[3].toString());
                response.addProperty("mobileNo", obj[4].toString());
                response.addProperty("birthDate", obj[5].toString() != null ? obj[5].toString() : "");
                res.add(response);
            }
            result.add("responseObject", res);
            result.addProperty("responseStatus", HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty(",message", "Failed to load Data");
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;
    }


    public Object saveStudentAdmission(Map<String, String> jsonRequest, HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            String rows = jsonRequest.get("row");
            JsonArray jsonArray = new JsonParser().parse(rows).getAsJsonArray();

            Branch branch = branchRepository.findByIdAndStatus(Long.parseLong(jsonRequest.get("branchId")), true);
            AcademicYear academicYear = academicYearRepository.findByIdAndStatus(Long.parseLong(jsonRequest.get("academicYearId")), true);
            Standard standard = standardRepository.findByIdAndStatus(Long.parseLong(jsonRequest.get("standardId")), true);
            Division division = divisionRepository.findByIdAndStatus(Long.parseLong(jsonRequest.get("divisionId")), true);

            List<StudentAdmission> studentAdmissions = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject studentRow = jsonArray.get(i).getAsJsonObject();

                StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(studentRow.get("studentId").getAsLong(), true);

                StudentAdmission savedStudentAdmission = admissionRepository.findByOutletIdAndBranchIdAndAcademicYearIdAndStudentRegisterId(users.getOutlet().getId(), branch.getId(), academicYear.getId(), studentRegister.getId());

                if (savedStudentAdmission == null) {
                    StudentAdmission studentAdmission = new StudentAdmission();
                    studentAdmission.setStudentRegister(studentRegister);
                    studentAdmission.setStatus(true);
                    studentAdmission.setOutlet(users.getOutlet());
                    studentAdmission.setBranch(branch);
                    studentAdmission.setAcademicYear(academicYear);
                    studentAdmission.setStandard(standard);
                    studentAdmission.setDivision(division);
                    studentAdmission.setStudentType(Integer.valueOf(jsonRequest.get("studentType")));
                    studentAdmission.setCreatedAt(LocalDateTime.now());
                    studentAdmission.setCreatedBy(users.getId());
                    studentAdmission.setIsRightOff(false);
                    studentAdmissions.add(studentAdmission);
                }
            }
            try {
                admissionRepository.saveAll(studentAdmissions);
                responseMessage.setMessage("Student promotion successfully done");
                responseMessage.setResponseStatus(HttpStatus.OK.value());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception " + e.getMessage());

                studentLogger.error("saveStudentAdmission -> failed to save student admission " + e);
                responseMessage.setMessage("Failed to promote student");
                responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
            studentLogger.error("saveStudentAdmission -> failed to save student admission");

            responseMessage.setMessage("Failed to save student admission");
            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }


    public JsonObject getStudentRegisterById(HttpServletRequest request) {
        JsonObject response = new JsonObject();
        JsonObject result = new JsonObject();
        try {
            StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(Long.parseLong(request.getParameter("id")), true);
            if (studentRegister != null) {
                response.addProperty("id", studentRegister.getId());
                response.addProperty("branchId", studentRegister.getBranch().getId());
                response.addProperty("branchName", studentRegister.getBranch().getBranchName());
                response.addProperty("firstName", studentRegister.getFirstName());
                response.addProperty("middleName", studentRegister.getMiddleName());
                response.addProperty("lastName", studentRegister.getLastName());
                response.addProperty("gender", studentRegister.getGender());
                response.addProperty("dob", studentRegister.getBirthDate() != null ? studentRegister.getBirthDate().toString() : "");
//                response.addProperty("age", studentRegister.getAge());
                response.addProperty("birthplace", studentRegister.getBirthPlace());
                response.addProperty("nationalityId", studentRegister.getNationality());
                response.addProperty("motherTongueId", String.valueOf(studentRegister.getMotherTongue() != null ? studentRegister.getMotherTongue().getId() : ""));
                response.addProperty("motherTongueName", studentRegister.getMotherTongue() != null ? studentRegister.getMotherTongue().getName() : "");
                response.addProperty("religionId", String.valueOf(studentRegister.getReligion() != null ? studentRegister.getReligion().getId() : ""));
                response.addProperty("religionName", studentRegister.getReligion() != null ? studentRegister.getReligion().getReligionName() : "");
                response.addProperty("casteId", studentRegister.getCaste() != null ? String.valueOf(studentRegister.getCaste().getId()) : "");
                response.addProperty("casteName", studentRegister.getCaste() != null ? studentRegister.getCaste().getCasteName() : "");
                response.addProperty("subCasteId", studentRegister.getSubCaste() != null ? String.valueOf(studentRegister.getSubCaste().getId()) : "");
                response.addProperty("subCasteName", studentRegister.getSubCaste() != null ? studentRegister.getSubCaste().getSubCasteName() : "");
                response.addProperty("categoryId", String.valueOf(studentRegister.getCasteCategory() != null ? studentRegister.getCasteCategory().getId() : ""));
                response.addProperty("categoryName", studentRegister.getCasteCategory() != null ? studentRegister.getCasteCategory().getCategoryName() : "");
                response.addProperty("hometown", studentRegister.getHomeTown());
                response.addProperty("aadharNo", studentRegister.getAadharNo());
                response.addProperty("saralId", studentRegister.getSaralId());
                response.addProperty("fatherName", studentRegister.getFatherName());
                response.addProperty("fatherOccupation", studentRegister.getFatherOccupation());
                response.addProperty("motherName", studentRegister.getMotherName());
                response.addProperty("motherOccupation", studentRegister.getMotherOccupation());
                response.addProperty("officeAddress", studentRegister.getOfficeAddress());
                response.addProperty("currentAddress", studentRegister.getCurrentAddress());
                response.addProperty("sameAsCurrentAddress", studentRegister.getSameAsCurrentAddress());
                response.addProperty("permanentAddress", studentRegister.getPermanentAddress());
                response.addProperty("phoneNoHome", studentRegister.getPhoneNoHome());
                response.addProperty("mobileNo", studentRegister.getMobileNo());
                response.addProperty("alternativeMobileNo", studentRegister.getAltMobileNo());
                response.addProperty("emailId", studentRegister.getEmailId());
                response.addProperty("generalRegisterNo", studentRegister.getGeneralRegisterNo());
                response.addProperty("nameOfPrevSchool", studentRegister.getNameOfPreviousSchool());
                response.addProperty("stdInPrevSchool", studentRegister.getStdInPreviousSchool());
                response.addProperty("result", Boolean.valueOf(studentRegister.getResult()));
                response.addProperty("dateOfAdmission", studentRegister.getDateOfAdmission() != null ? studentRegister.getDateOfAdmission().toString() : "");
                response.addProperty("admittedStandardId", String.valueOf(studentRegister.getAdmittedStandard() != null ? studentRegister.getAdmittedStandard().getId() : ""));
                response.addProperty("admittedStandard", studentRegister.getAdmittedStandard() != null ? studentRegister.getAdmittedStandard().getStandardName() : "");
                response.addProperty("studentIsOld", studentRegister.getTypeOfStudent());

                StudentAdmission studentAdmission = admissionRepository.findTop1ByStudentRegisterIdOrderByIdDesc(studentRegister.getId());
                if (studentAdmission != null) {
                    response.addProperty("academicYearId", studentAdmission.getAcademicYear().getId());
                    response.addProperty("academicYear", studentAdmission.getAcademicYear().getYear());
                    response.addProperty("currentStandardId", studentAdmission.getStandard().getId());
                    response.addProperty("currentStandardName", studentAdmission.getStandard().getStandardName());
                    response.addProperty("divisionId", String.valueOf(studentAdmission.getDivision() != null ? studentAdmission.getDivision().getId() : ""));
                    response.addProperty("division", studentAdmission.getDivision() != null ? studentAdmission.getDivision().getDivisionName() : "");
                    response.addProperty("studType", studentAdmission.getStudentType());
                    response.addProperty("studentGroup", String.valueOf(studentAdmission.getStudentGroup() != null ? studentAdmission.getStudentGroup() : ""));
                    response.addProperty("isHostel", studentAdmission.getIsHostel());
                    response.addProperty("isBusConcession", studentAdmission.getIsBusConcession());
                    response.addProperty("isVacation", studentAdmission.getIsVacation());

                    response.addProperty("busConcessionAmount", studentAdmission.getBusConcessionAmount());
                    response.addProperty("isScholarship", studentAdmission.getIsScholarship());
                    if (studentAdmission.getNts() != null) {

                        response.addProperty("nts", Boolean.valueOf(studentAdmission.getNts()));
                    }
                    response.addProperty("concession", studentAdmission.getConcessionAmount());
                } else {
                    response.addProperty("academicYearId", String.valueOf(studentRegister.getAcademicYear() != null ? studentRegister.getAcademicYear().getId() : ""));
                    response.addProperty("divisionId", String.valueOf(studentRegister.getDivision() != null ? studentRegister.getDivision().getId() : ""));
                    response.addProperty("currentStandardId", String.valueOf(studentRegister.getCurrentStandard() != null ? studentRegister.getCurrentStandard().getId() : ""));
                    response.addProperty("studType", studentRegister.getStudentType());
                    response.addProperty("studentGroup", studentRegister.getStudentGroup());
                    response.addProperty("isHostel", studentRegister.getIsHostel());
                    response.addProperty("isBusConcession", studentRegister.getIsBusConcession());
                    response.addProperty("busConcessionAmount", "");
                    response.addProperty("isScholarship", studentRegister.getIsScholarship());
                    response.addProperty("nts", Boolean.valueOf(studentRegister.getNts()));
                    response.addProperty("concession", "");
                }

                result.addProperty("message", "success");
                result.addProperty("responseStatus", HttpStatus.OK.value());
                result.add("data", response);
            } else {
                result.addProperty("message", "Data not found");
                result.addProperty("responseStatus", HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            studentLogger.error("getStudentRegisterById -> failed to update student register " + e.getMessage());
            response.addProperty("message", "Failed to update student register");
            response.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;
    }


    public Object updateStudent(MultipartHttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();

        FileStorageProperties fileStorageProperties = new FileStorageProperties();

        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        try {
            StudentRegister studentInfo = studentRegisterRepository.findByIdAndStatus(Long.parseLong(request.getParameter("id")), true);

            if (studentInfo != null) {
                studentInfo.setOutlet(users.getOutlet());

                Long branchId = Long.valueOf(request.getParameter("branchId"));
                Branch branch = branchRepository.findByIdAndStatus(branchId, true);
                studentInfo.setBranch(branch);

                studentInfo.setFirstName(request.getParameter("firstName"));
                studentInfo.setMiddleName(request.getParameter("middleName"));
                studentInfo.setLastName(request.getParameter("lastName"));
                studentInfo.setGender(request.getParameter("gender"));
                if (request.getParameterMap().containsKey("dob")) {
                    studentInfo.setBirthDate(LocalDate.parse(request.getParameter("dob")));
                }
                studentInfo.setAge(0L);
                if (request.getParameterMap().containsKey("age")) {
                    studentInfo.setAge(Long.valueOf(request.getParameter("age")));
                }
                if (request.getParameterMap().containsKey("birthPlace")) {
                    studentInfo.setBirthPlace(request.getParameter("birthPlace"));
                }
                studentInfo.setNationality(request.getParameter("nationalityId"));

                if (request.getParameterMap().containsKey("motherTongueId")) {
                    Long motherTongueId = Long.valueOf(request.getParameter("motherTongueId"));
                    MotherTongue motherTongue = motherTongueRepository.findByIdAndStatus(motherTongueId, true);
                    studentInfo.setMotherTongue(motherTongue);
                }
                if (request.getParameterMap().containsKey("religionId")) {
                    Long religionId = Long.valueOf(request.getParameter("religionId"));
                    Religion religion = religionRepository.findByIdAndStatus(religionId, true);
                    studentInfo.setReligion(religion);
                }
                if (request.getParameterMap().containsKey("casteId")) {
                    Long casteId = Long.valueOf(request.getParameter("casteId"));
                    Caste caste = casteRepository.findByIdAndStatus(casteId, true);
                    studentInfo.setCaste(caste);
                }
                if (request.getParameterMap().containsKey("subCasteId")) {
                    Long subCasteId = Long.valueOf(request.getParameter("subCasteId"));
                    SubCaste subCaste = subCasteRepository.findByIdAndStatus(subCasteId, true);
                    studentInfo.setSubCaste(subCaste);
                }
                if (request.getParameterMap().containsKey("categoryId")) {
                    Long categoryId = Long.valueOf(request.getParameter("categoryId"));
                    CasteCategory casteCategory = categoryRepository.findByIdAndStatus(categoryId, true);
                    studentInfo.setCasteCategory(casteCategory);
                }
                if (request.getParameterMap().containsKey("hometown")) {
                    studentInfo.setHomeTown(request.getParameter("hometown"));
                }
                if (request.getParameterMap().containsKey("aadharNo")) {
                    studentInfo.setAadharNo(request.getParameter("aadharNo"));
                }
                if (request.getParameterMap().containsKey("saralId")) {
                    studentInfo.setSaralId(request.getParameter("saralId"));
                }

                studentInfo.setFatherName(request.getParameter("fatherName"));
                if (request.getParameterMap().containsKey("fatherOccupation")) {
                    studentInfo.setFatherOccupation(request.getParameter("fatherOccupation"));
                }
                if (request.getParameterMap().containsKey("motherName")) {
                    studentInfo.setMotherName(request.getParameter("motherName"));
                }
                if (request.getParameterMap().containsKey("motherOccupation")) {
                    studentInfo.setMotherOccupation(request.getParameter("motherOccupation"));
                }
                if (request.getParameterMap().containsKey("officeAddress")) {
                    studentInfo.setOfficeAddress(request.getParameter("officeAddress"));
                }
                if (request.getParameterMap().containsKey("currentAddress")) {
                    studentInfo.setCurrentAddress(request.getParameter("currentAddress"));
                }
                if (request.getParameterMap().containsKey("sameAsCurrentAddress")) {
                    studentInfo.setSameAsCurrentAddress(Boolean.valueOf(request.getParameter("sameAsCurrentAddress")));
                }
                if (request.getParameterMap().containsKey("permanentAddress")) {
                    studentInfo.setPermanentAddress(request.getParameter("permanentAddress"));
                }
                if (request.getParameterMap().containsKey("phoneNoHome")) {
                    studentInfo.setPhoneNoHome(Long.valueOf(request.getParameter("phoneNoHome")));
                }
                if (request.getParameterMap().containsKey("mobileNo")) {
                    studentInfo.setMobileNo(Long.valueOf(request.getParameter("mobileNo")));
                }
                if (request.getParameterMap().containsKey("alternativeMobileNo")) {
                    studentInfo.setAltMobileNo(Long.valueOf(request.getParameter("alternativeMobileNo")));
                }
                if (request.getParameterMap().containsKey("emailId")) {
                    studentInfo.setEmailId(request.getParameter("emailId"));
                }
                if (request.getParameterMap().containsKey("generalRegisterNo")) {
                    studentInfo.setGeneralRegisterNo(request.getParameter("generalRegisterNo"));
                }
                if (request.getParameterMap().containsKey("nameOfPrevSchool")) {
                    studentInfo.setNameOfPreviousSchool(request.getParameter("nameOfPrevSchool"));
                }
                if (request.getParameterMap().containsKey("stdInPrevSchool")) {
                    studentInfo.setStdInPreviousSchool(request.getParameter("stdInPrevSchool"));
                }
                if (request.getParameterMap().containsKey("result")) {
                    studentInfo.setResult(request.getParameter("result"));
                }
                studentInfo.setStatus(true);

                if (request.getParameterMap().containsKey("doa")) {
                    studentInfo.setDateOfAdmission(LocalDate.parse(request.getParameter("doa")));
                }

                if (request.getParameterMap().containsKey("studentIsOld")) {
                    studentInfo.setTypeOfStudent(request.getParameter("studentIsOld"));
                }

                if (Integer.valueOf(request.getParameter("studentIsOld")) == 1) {
                    Long standardId = Long.valueOf(request.getParameter("admittedStandardId"));
                    Standard standard = standardRepository.findByIdAndStatus(standardId, true);
                    studentInfo.setAdmittedStandard(standard);
                    studentInfo.setTypeOfStudent(request.getParameter("studentIsOld"));
                }

                try {
                    StudentRegister savedStudentInfo = studentRegisterRepository.save(studentInfo);

                    boolean flag = false;
                    /*StudentAdmission studentAdmission1 = null;
                    List<StudentAdmission> studentAdmissions = admissionRepository.findByStudentRegisterId(savedStudentInfo.getId());
                    if (studentAdmissions.size() > 0) {
                        StudentAdmission studentAdmission = admissionRepository.findTop1ByStudentRegisterIdOrderByIdDesc(savedStudentInfo.getId());
                        if (studentAdmission != null) {
                            studentAdmission.setStatus(true);
                            studentAdmission.setOutlet(users.getOutlet());
                            studentAdmission.setBranch(savedStudentInfo.getBranch());
                            studentAdmission.setStudentRegister(savedStudentInfo);
                            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
                            AcademicYear academicYear = academicYearRepository.findByIdAndStatus(academicYearId, true);
                            studentAdmission.setAcademicYear(academicYear);

                            Long currentStandardId = Long.valueOf(request.getParameter("currentStandardId"));
                            Standard currentStandard = standardRepository.findByIdAndStatus(currentStandardId, true);
                            studentAdmission.setStandard(currentStandard);

                            if (request.getParameterMap().containsKey("divisionId")) {
                                Long divisionId = Long.valueOf(request.getParameter("divisionId"));
                                Division division = divisionRepository.findByIdAndStatus(divisionId, true);
                                studentAdmission.setDivision(division);
                            }

                            studentAdmission.setStudentType(Integer.valueOf(request.getParameter("studentType")));
                            if (request.getParameterMap().containsKey("studentGroup")) {
                                studentAdmission.setStudentGroup(Integer.valueOf(request.getParameter("studentGroup")));
                            } else {
                                studentAdmission.setStudentGroup(null);
                            }
                            studentAdmission.setIsHostel(Boolean.valueOf(request.getParameter("isHostel")));

                            if (studentAdmission.getIsBusConcession() != Boolean.valueOf(request.getParameter("isBusConcession"))) {
                                flag = true;
                            }

                            studentAdmission.setIsBusConcession(Boolean.valueOf(request.getParameter("isBusConcession")));

                            if (studentAdmission.getIsVacation() != Boolean.valueOf(request.getParameter("isVacation"))) {
                                flag = true;
                            }

                            studentAdmission.setIsVacation(Boolean.valueOf(request.getParameter("isVacation")));

                            studentAdmission.setBusConcessionAmount(0L);
                            if (request.getParameterMap().containsKey("busConcessionAmount")) {
                                studentAdmission.setBusConcessionAmount(Long.valueOf(request.getParameter("busConcessionAmount")));
                            }

                            studentAdmission.setIsScholarship(Boolean.valueOf(request.getParameter("isScholarship")));
                            studentAdmission.setNts(request.getParameter("nts"));
                            if (request.getParameterMap().containsKey("concession")) {
                                studentAdmission.setConcessionAmount(Long.valueOf(request.getParameter("concession")));
                            }

                            studentAdmission1 = admissionRepository.save(studentAdmission);
                        }
                    } else {
                        if (request.getParameterMap().containsKey("academicYearId") && request.getParameterMap().containsKey("currentStandardId")) {
                            System.out.println("As a new record updating");
                            StudentAdmission stdAdmission = new StudentAdmission();
                            stdAdmission.setOutlet(users.getOutlet());
                            stdAdmission.setBranch(savedStudentInfo.getBranch());
                            stdAdmission.setStudentRegister(savedStudentInfo);
                            stdAdmission.setCreatedAt(LocalDateTime.now());
                            stdAdmission.setCreatedBy(users.getId());
                            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
                            AcademicYear academicYear = academicYearRepository.findByIdAndStatus(academicYearId, true);
                            stdAdmission.setAcademicYear(academicYear);

                            Long currentStandardId = Long.valueOf(request.getParameter("currentStandardId"));
                            Standard currentStandard = standardRepository.findByIdAndStatus(currentStandardId, true);
                            stdAdmission.setStandard(currentStandard);

                            if (request.getParameterMap().containsKey("divisionId")) {
                                Long divisionId = Long.valueOf(request.getParameter("divisionId"));
                                Division division = divisionRepository.findByIdAndStatus(divisionId, true);
                                stdAdmission.setDivision(division);
                            }

                            stdAdmission.setStudentType(Integer.valueOf(request.getParameter("studentType")));
                            if (request.getParameterMap().containsKey("studentGroup")) {
                                stdAdmission.setStudentGroup(Integer.valueOf(request.getParameter("studentGroup")));
                            } else {
                                stdAdmission.setStudentGroup(null);
                            }
                            stdAdmission.setIsHostel(Boolean.valueOf(request.getParameter("isHostel")));
                            stdAdmission.setIsBusConcession(Boolean.valueOf(request.getParameter("isBusConcession")));

                            stdAdmission.setBusConcessionAmount(0L);
                            if (request.getParameterMap().containsKey("busConcessionAmount")) {
                                stdAdmission.setBusConcessionAmount(Long.valueOf(request.getParameter("busConcessionAmount")));
                            }
                            stdAdmission.setIsScholarship(Boolean.valueOf(request.getParameter("isScholarship")));
                            stdAdmission.setNts(request.getParameter("nts"));
                            if (request.getParameterMap().containsKey("concession")) {
                                stdAdmission.setConcessionAmount(Long.valueOf(request.getParameter("concession")));
                            }

                            studentAdmission1 = admissionRepository.save(stdAdmission);
                        }
                    }*/

                    if (request.getFile("studentImage") != null) {
                        if (studentInfo.getStudentImage() != null) {
                            File oldFile = new File("." + studentInfo.getStudentImage());

                            if (oldFile.exists()) {
                                System.out.println("Document Deleted");
                                //remove file from local directory
                                if (!oldFile.delete()) {
                                    responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                                    responseMessage.setMessage("Failed to delete old documents. Please try again!");
                                    return responseMessage;
                                }
                            }
                        }

                        MultipartFile image = request.getFile("studentImage");
                        fileStorageProperties.setUploadDir("./uploads" + File.separator + savedStudentInfo.getId() + File.separator + "student" + File.separator);
                        String imagePath = fileStorageService.storeFile(image, fileStorageProperties);

                        if (imagePath != null) {
                            savedStudentInfo.setStudentImage("/uploads" + File.separator + savedStudentInfo.getId() + File.separator + "student" + File.separator + imagePath);
                        } else {
                            studentLogger.error("Failed to upload father image. Please try again! ");
                        }
                    }
                    if (request.getFile("fatherImage") != null) {

                        if (studentInfo.getFatherImage() != null) {
                            File oldFile = new File("." + studentInfo.getFatherImage());

                            if (oldFile.exists()) {
                                System.out.println("Document Deleted");
                                //remove file from local directory
                                if (!oldFile.delete()) {
                                    responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                                    responseMessage.setMessage("Failed to delete old documents. Please try again!");
                                    return responseMessage;
                                }
                            }
                        }

                        MultipartFile image = request.getFile("fatherImage");
                        fileStorageProperties.setUploadDir("./uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator);
                        String imagePath = fileStorageService.storeFile(image, fileStorageProperties);

                        if (imagePath != null) {
                            savedStudentInfo.setFatherImage("/uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator + imagePath);
                        } else {
                            studentLogger.error("Failed to upload father image. Please try again! ");
                        }
                    }
                    if (request.getFile("motherImage") != null) {
                        if (studentInfo.getMotherImage() != null) {
                            File oldFile = new File("." + studentInfo.getMotherImage());

                            if (oldFile.exists()) {
                                System.out.println("Document Deleted");
                                //remove file from local directory
                                if (!oldFile.delete()) {
                                    responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                                    responseMessage.setMessage("Failed to delete old documents. Please try again!");
                                    return responseMessage;
                                }
                            }
                        }
                        MultipartFile image = request.getFile("motherImage");
                        fileStorageProperties.setUploadDir("./uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator);
                        String imagePath = fileStorageService.storeFile(image, fileStorageProperties);

                        if (imagePath != null) {
                            savedStudentInfo.setMotherImage("/uploads" + File.separator + savedStudentInfo.getId() + File.separator + "parentDocs" + File.separator + imagePath);
                        } else {
                            studentLogger.error("Failed to upload mother image. Please try again! ");
                        }
                    }

                    /*if (studentAdmission1 != null) {
                        FeesMaster feesMaster = null;

                        if (studentAdmission1.getStudentGroup() != null)
                            feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStudentGroupAndStatus(
                                    savedStudentInfo.getBranch().getId(), studentAdmission1.getStandard().getId(), studentAdmission1.getAcademicYear().getId(),
                                    studentAdmission1.getStudentType(), studentAdmission1.getStudentGroup(), true);
                        else
                            feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStatus(
                                    savedStudentInfo.getBranch().getId(), studentAdmission1.getStandard().getId(), studentAdmission1.getAcademicYear().getId(), studentAdmission1.getStudentType(), true);
                        if (feesMaster != null) {
                            FeesTransactionSummary feesTransactionSummary1 = feesTransactionSummaryRepository.findByFeesMasterIdAndStudentRegisterIdAndAcademicYearId(feesMaster.getId(), studentAdmission1.getStudentRegister().getId(), studentAdmission1.getAcademicYear().getId());
                            double outstanding = 0.0;
                            if (feesTransactionSummary1 != null) {
                                System.out.println("As a new record updating in fees transaction " + studentAdmission1.getStudentRegister().getId());

                                if (studentAdmission1.getStudentType() == 2 && (studentAdmission1.getStandard().getStandardName().equalsIgnoreCase("11") ||
                                        feesMaster.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                    if (savedStudentInfo.getGender().equalsIgnoreCase("male")) {
                                        feesTransactionSummary1.setBalance(feesMaster.getAmountForBoy());
                                        feesTransactionSummary1.setTotalFees(feesMaster.getAmountForBoy());
                                        outstanding = feesMaster.getAmountForBoy();
                                    } else if (savedStudentInfo.getGender().equalsIgnoreCase("female")) {
                                        feesTransactionSummary1.setBalance(feesMaster.getAmountForGirl());
                                        feesTransactionSummary1.setTotalFees(feesMaster.getAmountForGirl());
                                        outstanding = feesMaster.getAmountForGirl();
                                    }
                                } else {
                                    feesTransactionSummary1.setBalance(feesMaster.getAmount());
                                    feesTransactionSummary1.setTotalFees(feesMaster.getAmount());
                                    outstanding = feesMaster.getAmount();
                                }


//                                if(flag == true)
                                {
                                    double headFee = getBusHeadFee(savedStudentInfo, feesMaster);
                                    if (studentAdmission1.getIsBusConcession() == false) {
                                        outstanding = outstanding - headFee;
                                        double totalFees = outstanding;

                                        feesTransactionSummary1.setTotalFees(totalFees);
                                        feesTransactionSummary1.setBalance(outstanding);
                                    }
                                }

                                feesTransactionSummary1.setPaidAmount(0.0);
                                feesTransactionSummary1.setStudentRegister(savedStudentInfo);
                                feesTransactionSummary1.setStandard(studentAdmission1.getStandard());
                                feesTransactionSummary1.setDivision(studentAdmission1.getDivision());
                                feesTransactionSummary1.setFeesMaster(feesMaster);
                                feesTransactionSummary1.setAcademicYear(studentAdmission1.getAcademicYear());
                                feesTransactionSummary1.setStudentType(studentAdmission1.getStudentType());
                                feesTransactionSummary1.setBranch(studentAdmission1.getBranch());
                                feesTransactionSummary1.setOutlet(studentAdmission1.getOutlet());
                                feesTransactionSummary1.setStudentGroup(studentAdmission1.getStudentGroup());
                                feesTransactionSummary1.setCreatedBy(users.getId());
                                feesTransactionSummary1.setStatus(true);

                                feesTransactionSummaryRepository.save(feesTransactionSummary1);
                            } else {
                                FeesTransactionSummary feesTransactionSummary = new FeesTransactionSummary();
                                System.out.println("As a new record updating feesTransactionSummary new " + studentAdmission1.getStudentRegister().getId());
                                if (studentAdmission1.getStudentType() == 2 && (studentAdmission1.getStandard().getStandardName().equalsIgnoreCase("11") ||
                                        feesMaster.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                    if (savedStudentInfo.getGender().equalsIgnoreCase("male")) {
                                        feesTransactionSummary.setBalance(feesMaster.getAmountForBoy());
                                        feesTransactionSummary.setTotalFees(feesMaster.getAmountForBoy());
                                        outstanding = feesMaster.getAmountForBoy();
                                    } else if (savedStudentInfo.getGender().equalsIgnoreCase("female")) {
                                        feesTransactionSummary.setBalance(feesMaster.getAmountForGirl());
                                        feesTransactionSummary.setTotalFees(feesMaster.getAmountForGirl());
                                        outstanding = feesMaster.getAmountForGirl();
                                    }
                                } else {
                                    feesTransactionSummary.setBalance(feesMaster.getAmount());
                                    feesTransactionSummary.setTotalFees(feesMaster.getAmount());
                                    outstanding = feesMaster.getAmount();
                                }

//                                if(flag == true)
                                {
                                    double headFee = getBusHeadFee(savedStudentInfo, feesMaster);
                                    if (studentAdmission1.getIsBusConcession() == false) {
                                        outstanding = outstanding - headFee;
                                        double totalFees = outstanding;

                                        feesTransactionSummary.setTotalFees(totalFees);
                                        feesTransactionSummary.setBalance(outstanding);
                                    }
                                }

                                feesTransactionSummary.setPaidAmount(0.0);
                                feesTransactionSummary.setStudentRegister(savedStudentInfo);
                                feesTransactionSummary.setStandard(studentAdmission1.getStandard());
                                feesTransactionSummary.setDivision(studentAdmission1.getDivision());
                                feesTransactionSummary.setFeesMaster(feesMaster);
                                feesTransactionSummary.setAcademicYear(studentAdmission1.getAcademicYear());
                                feesTransactionSummary.setStudentType(studentAdmission1.getStudentType());
                                feesTransactionSummary.setBranch(studentAdmission1.getBranch());
                                feesTransactionSummary.setOutlet(studentAdmission1.getOutlet());
                                feesTransactionSummary.setStudentGroup(studentAdmission1.getStudentGroup());
                                feesTransactionSummary.setCreatedBy(users.getId());
                                feesTransactionSummary.setStatus(true);

                                feesTransactionSummaryRepository.save(feesTransactionSummary);
                            }

                            double hostelFee = getHostelHeadFee(savedStudentInfo, feesMaster);
                            double totalFeeExceptHostel = outstanding - hostelFee;

                            *//*LedgerMaster ledgerMaster = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(savedStudentInfo.getId(), users.getBranch().getId(), true);
                            if (ledgerMaster != null) {
                                TranxSalesInvoice invoiceTranx = tranxSalesInvoiceRepository.findBySundryDebtorsId(ledgerMaster.getId());
                                if (invoiceTranx != null) {
                                    System.out.println("sales invoice already saved");
                                } else {
                                    System.out.println("new createTranxSalesInvoice for student " + ledgerMaster.getId());

                                    createTranxSalesInvoice(studentInfo, users, ledgerMaster.getId(), outstanding, feesMaster, totalFeeExceptHostel);
                                }
                            } else {
                                LedgerMaster ledgerMaster1 = createLedgerForStudent(studentInfo, users);
                                System.out.println("new ledger created for student " + ledgerMaster1.getId());
                                createTranxSalesInvoice(studentInfo, users, ledgerMaster1.getId(), outstanding, feesMaster, totalFeeExceptHostel);
                                System.out.println("new createTranxSalesInvoice for student " + ledgerMaster1.getId());
                            }*//*
                        }
                    }*/

                    try {
                        studentRegisterRepository.save(savedStudentInfo);
                        String studName = savedStudentInfo.getFirstName();
                        if (savedStudentInfo.getFatherName() != null)
                            studName = studName + " " + savedStudentInfo.getFatherName();
                        if (savedStudentInfo.getLastName() != null)
                            studName = studName + " " + savedStudentInfo.getLastName();
                        ledgerMasterRepository.updateLedgerNameByStudentId(savedStudentInfo.getId(), studName);
                        responseMessage.setMessage("Student update successfully");
                        responseMessage.setResponseStatus(HttpStatus.OK.value());
                    } catch (Exception e) {
                        studentLogger.error("update student images exception " + e.getMessage());
                        System.out.println("update student images Exception " + e.getMessage());
                        e.printStackTrace();
                        responseMessage.setMessage("Failed to update student");
                        responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                } catch (Exception e) {
                    studentLogger.error("update student exception " + e.getMessage());
                    System.out.println("Exception " + e.getMessage());
                    e.printStackTrace();
                    responseMessage.setMessage("Failed to update student");
                    responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            } else {
                responseMessage.setMessage("Student not found");
                responseMessage.setResponseStatus(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            studentLogger.error("update student exception " + e.getMessage());
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
            responseMessage.setMessage("Failed to update student");
            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }

    private double getBusHeadFee(StudentRegister savedStudentInfo, FeesMaster feesMaster) {
        double headFee = 0;
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesMaster.getId(), true);
        for (FeesDetails feesDetails : feesDetailsList) {
            System.out.println("feesDetails.getFeeHead() ->>" + feesDetails.getFeeHead().getFeeHeadName());
            boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("bus"); // true
            System.out.println("Bus isFound => " + isFound);
            if (isFound) {
                if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                    if (savedStudentInfo.getGender().equalsIgnoreCase("male")) {
                        headFee = feesDetails.getAmountForBoy();
                    } else if (savedStudentInfo.getGender().equalsIgnoreCase("female")) {
                        headFee = feesDetails.getAmountForGirl();
                    }
                } else {
                    headFee = feesDetails.getAmount();
                }
                System.out.println("headFee => " + headFee);
            }
        }
        return headFee;
    }

    private double getMtsFee(StudentRegister savedStudentInfo, FeesMaster feesMaster) {

        double headFee = 0;
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesMaster.getId(), true);
        for (FeesDetails feesDetails : feesDetailsList) {
            System.out.println("feeDetails.getFeeHead()" + feesDetails.getFeeHead().getFeeHeadName());
            boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("mts");//true
            System.out.println("Mts Found" + isFound);
            if (isFound) {
                if (feesMaster.getStandard().getStandardName().equalsIgnoreCase("8") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("9")) {
                    headFee = feesDetails.getAmount();
                }
                System.out.println("headFee" + headFee);
            }
        }
        return headFee;
    }


    private double getScholarshipFee(StudentRegister savedStudentInfo, FeesMaster feesMaster) {
        double headFee = 0;
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesMaster.getId(), true);
        for (FeesDetails feesDetails : feesDetailsList) {
            System.out.println("feesDetails.getFeeHead()" + feesDetails.getFeeHead().getFeeHeadName());
            boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("scholarship fee");
            System.out.println("Scholarship found" + isFound);
            if (isFound) {
                if (feesMaster.getStandard().getStandardName().equalsIgnoreCase("5") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("8")) {
                    headFee = feesDetails.getAmount();
                }
                System.out.println("headFee" + headFee);
            }
        }
        return headFee;
    }

    private double getFoundationFee(StudentRegister savedStudentInfo, FeesMaster feesMaster) {

        double headFee = 0;
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesMaster.getId(), true);
        for (FeesDetails feesDetails : feesDetailsList) {
            System.out.println("feesDetails.getFeeHead()" + feesDetails.getFeeHead().getFeeHeadName());
            boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("foundation");
            System.out.println("Foundation Found" + isFound);
            if (isFound) {
                if (feesMaster.getStandard().getStandardName().equalsIgnoreCase("8") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("9") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("10")) {
                    headFee = feesDetails.getAmount();

                }
                System.out.println("headFee" + headFee);
            }
        }
        return headFee;
    }

    private double getNtsFee(StudentRegister savedStudentInfo, FeesMaster feesMaster) {
        double headFee = 0;
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesMaster.getId(), true);
        for (FeesDetails feesDetails : feesDetailsList) {
            System.out.println("feesDetails.getFeeHead()" + feesDetails.getFeeHead().getFeeHeadName());
            boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("nts");
            System.out.println("NTs found" + isFound);
            if (isFound) {
                if (feesMaster.getStandard().getStandardName().equalsIgnoreCase("10")) {
                    headFee = feesDetails.getAmount();

                }
                System.out.println("headFee" + headFee);
            }
        }
        return headFee;
    }


    private double getVacationHeadFee(StudentRegister savedStudentInfo, FeesMaster feesMaster) {
        double headFee = 0;
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesMaster.getId(), true);
        for (FeesDetails feesDetails : feesDetailsList) {
            System.out.println("feesDetails.getFeeHead() ->>" + feesDetails.getFeeHead().getFeeHeadName());
            boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("vacation"); // true
            System.out.println("getVacationHeadFee isFound => " + isFound);
            if (isFound) {
                if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                    if (savedStudentInfo.getGender().equalsIgnoreCase("male")) {
                        headFee = feesDetails.getAmountForBoy();
                    } else if (savedStudentInfo.getGender().equalsIgnoreCase("female")) {
                        headFee = feesDetails.getAmountForGirl();
                    }
                } else {
                    headFee = feesDetails.getAmount();
                }
                System.out.println("headFee => " + headFee);
            }
        }
        return headFee;
    }

    public double getHostelHeadFees(StudentRegister savedStudentInfo, FeesMaster feesMaster) {
        double headFee = 0;
        System.out.println("feesMaster " + feesMaster.getId());
        String amountRow = feesDetailsRepository.getHostelHeadAmountByFeesMaster(feesMaster.getId(), false, true);

        if (amountRow != null) {
            String[] amounts = amountRow.split(",");
            System.out.println("amounts length:" + amounts.length);
            if (amounts.length > 0) {
                if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                    if (savedStudentInfo.getGender().equalsIgnoreCase("male")) {
                        headFee = Double.parseDouble(amounts[1]);
                    } else if (savedStudentInfo.getGender().equalsIgnoreCase("female")) {
                        headFee = Double.parseDouble(amounts[2]);
                    }
                } else {
                    headFee = Double.parseDouble(amounts[0]);
                }
            }
        }
        System.out.println("headFee => " + headFee);
        return headFee;
    }

    public double getHostelHeadFee(StudentRegister savedStudentInfo, FeesMaster feesMaster) {
        double headFee = 0;
        System.out.println("feesMaster " + feesMaster.getId());
        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesMaster.getId(), true);
        for (FeesDetails feesDetails : feesDetailsList) {
            System.out.println("feesDetails.getFeeHead() ->>" + feesDetails.getFeeHead().getFeeHeadName());
            boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("hostel"); // true
            System.out.println("Bus isFound => " + isFound);
            if (isFound) {
                if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                    if (savedStudentInfo.getGender().equalsIgnoreCase("male")) {
                        headFee = feesDetails.getAmountForBoy();
                    } else if (savedStudentInfo.getGender().equalsIgnoreCase("female")) {
                        headFee = feesDetails.getAmountForGirl();
                    }
                } else {
                    headFee = feesDetails.getAmount();
                }
                System.out.println("headFee => " + headFee);
            }
        }
        return headFee;
    }

    public LedgerMaster createLedgerForStudent(StudentRegister studentRegister, Users users) {
        LedgerMaster ledgerMaster = new LedgerMaster();
        PrincipleGroups groups = null;
        Principles principles = null;
        Foundations foundations = null;
        LedgerMaster mLedger = null;
        principles = principleRepository.findByIdAndStatus(1, true);
        foundations = principles.getFoundations();

        groups = principleGroupsRepository.findByIdAndStatus(1, true);
        if (groups != null) {
            ledgerMaster.setPrincipleGroups(groups);
            ledgerMaster.setPrinciples(principles);
            ledgerMaster.setUniqueCode(groups.getUniqueCode());
        } else {
            ledgerMaster.setPrinciples(principles);
            ledgerMaster.setUniqueCode(principles.getUniqueCode());
        }
        if (foundations != null) {
            ledgerMaster.setFoundations(foundations);
        }
        ledgerMaster.setIsPrivate(false);
        ledgerMaster.setIsDeleted(false); //isDelete : true means , we can delete this ledger
        ledgerMaster.setStatus(true);
        ledgerMaster.setIsDefaultLedger(false);

        if (users.getBranch() != null) ledgerMaster.setBranch(users.getBranch());
        ledgerMaster.setOutlet(users.getOutlet());
        ledgerMaster.setCreatedBy(users.getId());
        String studName = studentRegister.getFirstName();
        if (studentRegister.getFatherName() != null) studName = studName + " " + studentRegister.getFatherName();
        if (studentRegister.getLastName() != null) studName = studName + " " + studentRegister.getLastName();
        ledgerMaster.setLedgerName(studName);
        ledgerMaster.setSlugName("sundry_debtors");
        ledgerMaster.setStudentRegister(studentRegister);
        ledgerMaster.setUnderPrefix("PG#1");

        ledgerMaster.setTaxType("NA");
        ledgerMaster.setMailingName(studName);
        ledgerMaster.setOpeningBalType("Dr");

        if (studentRegister.getCurrentAddress() != null) {
            ledgerMaster.setAddress(studentRegister.getCurrentAddress());
        }
        ledgerMaster.setPincode(0L);
        if (studentRegister.getEmailId() != null) {
            ledgerMaster.setEmail(studentRegister.getEmailId());
        } else {
            ledgerMaster.setEmail("NA");
        }
        ledgerMaster.setMobile(studentRegister.getMobileNo());
        ledgerMaster.setTaxable(false);
        ledgerMaster.setBankName("NA");
        ledgerMaster.setAccountNumber("NA");
        ledgerMaster.setIfsc("NA");
        ledgerMaster.setBankBranch("NA");
        ledgerMaster.setOpeningBal(0.0);

        /* pune demo visit changes */
        ledgerMaster.setCreditDays(0);
        ledgerMaster.setApplicableFrom("NA");
        ledgerMaster.setFoodLicenseNo("NA");

        LedgerMaster ledgerMaster1 = ledgerMasterRepository.save(ledgerMaster);
        return ledgerMaster1;
    }

    /* Posting into Sundry Debtors */
    private void insertIntoTranxDetailSD(TranxSalesInvoice mSalesTranx, TransactionTypeMaster tranxType, String key) {
        try {

//            ledgerTransactionDetailsRepository.insertIntoLegerTranxDetail(
           /* ledgerTransactionDetailsRepository.insertIntoLegerTranxDetailWithTranxDate(mSalesTranx.getSundryDebtors().getFoundations().getId(), mSalesTranx.getSundryDebtors().getPrinciples().getId(),
                    mSalesTranx.getSundryDebtors().getPrincipleGroups().getId(), null, tranxType.getId(), null,
                    mSalesTranx.getBranch() != null ? mSalesTranx.getBranch().getId() : null, mSalesTranx.getOutlet().getId(), "pending",
                    mSalesTranx.getTotalAmount() * -1, 0.0, mSalesTranx.getBillDate() != null ? mSalesTranx.getBillDate() : null, null,
                    mSalesTranx.getId(), tranxType.getTransactionName() != null ? tranxType.getTransactionName() : null,
                    mSalesTranx.getSundryDebtors() != null ? mSalesTranx.getSundryDebtors().getUnderPrefix() : null,
                    mSalesTranx.getFinancialYear() != null ? mSalesTranx.getFinancialYear() : null, mSalesTranx.getCreatedBy(),
                    mSalesTranx.getSundryDebtors() != null ? mSalesTranx.getSundryDebtors().getId() : null, mSalesTranx.getSalesInvoiceNo() != null ? mSalesTranx.getSalesInvoiceNo() : null, true);
*/
            ledgerCommonPostings.callToPostings(mSalesTranx.getTotalAmount(), mSalesTranx.getSundryDebtors(), tranxType,
                    mSalesTranx.getSundryDebtors().getAssociateGroups(), mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(),
                    mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(),
                    "DR", true, tranxType.getTransactionCode(), key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Store Procedure Error " + e.getMessage());
        }
    }/* End of Posting into Sundry Debtors */

    /* Posting into Sales Accounts : Fees Heads*/
    private void insertIntoTranxDetailFA(StudentRegister studentRegister, TranxSalesInvoice mSalesTranx, TransactionTypeMaster tranxType, FeesMaster feesMaster, String key) {
        try {

            StudentAdmission studentAdmission = studentAdmissionRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(studentRegister.getId(), feesMaster.getAcademicYear().getId(), true);
//            StudentAdmission studentAdmission = studentAdmissionRepository.findByStudentRegisterIdAndStatus(studentRegister.getId(), true);
            List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatusAndHostelsOnly(feesMaster.getId(), true, true);

            for (FeesDetails feesDetails : feesDetailsList) {

                if (!feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("hostel") && !feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("bus")
                        && !feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("vacation") && !feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("foundation")
                        && !feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("mts") && !feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("nts")
                        && !feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("scholarship")) {
                    double fees = 0;
                    if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11")
                            || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("male")) {
                        fees = feesDetails.getAmountForBoy();
                    } else if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11")
                            || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("female")) {
                        fees = feesDetails.getAmountForGirl();
                    } else {
                        fees = feesDetails.getAmount();
                    }


                    LedgerTransactionPostings ledgerTransactionDetails = ledgerTransactionPostingsRepository.findByTransactionIdAndLedgerMasterIdAndStatus(mSalesTranx.getId(), feesDetails.getFeeHead().getLedger().getId(), true);
                    if (ledgerTransactionDetails == null) {
                        ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType, feesDetails.getFeeHead().getLedger().getAssociateGroups(), mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);
                    } else {
                        try {
                            /* *** New Postings Logic *****/
                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(feesDetails.getFeeHead().getLedger().getId(), 17L, mSalesTranx.getId(), true);
                            if (mLedger != null) {
                                mLedger.setAmount(fees);
                                mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                mLedger.setOperations("Updated");
                                ledgerTransactionPostingsRepository.save(mLedger);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                        }
                    }

                    /******* Insert into Sales Invoice Details against Fees Head *****/
                    TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
                    invoiceDetails.setAmount(fees);
                    if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
                    invoiceDetails.setOutlet(mSalesTranx.getOutlet());
                    if (mSalesTranx.getFiscalYear() != null) invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
                    invoiceDetails.setTranxSalesInvoice(mSalesTranx);
                    invoiceDetails.setFeeHead(feesDetails.getFeeHead());
                    invoiceDetails.setStatus(true);
                    try {
                        tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
                    } catch (Exception ex) {
                        studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
                    }
                } else if (feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("vacation") && studentAdmission.getIsVacation()) { // if student applicable of vacation
                    double fees = 0;
                    if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("male")) {
                        fees = feesDetails.getAmountForBoy();
                    } else if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("female")) {
                        fees = feesDetails.getAmountForGirl();
                    } else {
                        fees = feesDetails.getAmount();
                    }

                    LedgerTransactionPostings ledgerTransactionDetails = ledgerTransactionPostingsRepository.findByTransactionIdAndLedgerMasterIdAndStatus(mSalesTranx.getId(), feesDetails.getFeeHead().getLedger().getId(), true);

                    if (ledgerTransactionDetails == null) {

                        ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType, feesDetails.getFeeHead().getLedger().getAssociateGroups(), mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);

                    } else {
                        try {
                            /**** New Postings Logic *****/
                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(feesDetails.getFeeHead().getLedger().getId(), 17L, mSalesTranx.getId(), true);
                            if (mLedger != null) {
                                mLedger.setAmount(fees);
                                mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                mLedger.setOperations("Updated");
                                ledgerTransactionPostingsRepository.save(mLedger);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                        }
                    }

                    /******* Insert into Sales Invoice Details against Fees Head *****/
                    TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
                    invoiceDetails.setAmount(fees);
                    if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
                    invoiceDetails.setOutlet(mSalesTranx.getOutlet());
                    if (mSalesTranx.getFiscalYear() != null) invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
                    invoiceDetails.setTranxSalesInvoice(mSalesTranx);
                    invoiceDetails.setFeeHead(feesDetails.getFeeHead());
                    invoiceDetails.setStatus(true);
                    try {
                        tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
                    } catch (Exception ex) {
                        studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
                    }
                } else if (feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("scholarship") && studentAdmission.getIsScholarship()) { // if student applicable of Scholarship
                    double fees = 0;
                    if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("male")) {
                        fees = feesDetails.getAmountForBoy();
                    } else if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("female")) {
                        fees = feesDetails.getAmountForGirl();
                    } else {
                        fees = feesDetails.getAmount();
                    }

                    LedgerTransactionPostings ledgerTransactionDetails = ledgerTransactionPostingsRepository.findByTransactionIdAndLedgerMasterIdAndStatus(mSalesTranx.getId(), feesDetails.getFeeHead().getLedger().getId(), true);

                    if (ledgerTransactionDetails == null) {
                        ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType, feesDetails.getFeeHead().getLedger().getAssociateGroups(), mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);

                    } else {
                        try {
                            /**** New Postings Logic *****/
                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(feesDetails.getFeeHead().getLedger().getId(), 17L, mSalesTranx.getId(), true);
                            if (mLedger != null) {
                                mLedger.setAmount(fees);
                                mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                mLedger.setOperations("Updated");
                                ledgerTransactionPostingsRepository.save(mLedger);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                        }
                    }

                    /******* Insert into Sales Invoice Details against Fees Head *****/
                    TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
                    invoiceDetails.setAmount(fees);
                    if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
                    invoiceDetails.setOutlet(mSalesTranx.getOutlet());
                    if (mSalesTranx.getFiscalYear() != null) invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
                    invoiceDetails.setTranxSalesInvoice(mSalesTranx);
                    invoiceDetails.setFeeHead(feesDetails.getFeeHead());
                    invoiceDetails.setStatus(true);
                    try {
                        tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
                    } catch (Exception ex) {
                        studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
                    }
                } else if (feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("nts") && studentAdmission.getNts()) { // if student applicable of Scholarship
                    double fees = 0;
                    if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("male")) {
                        fees = feesDetails.getAmountForBoy();
                    } else if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("female")) {
                        fees = feesDetails.getAmountForGirl();
                    } else {
                        fees = feesDetails.getAmount();
                    }

                    LedgerTransactionPostings ledgerTransactionDetails = ledgerTransactionPostingsRepository.findByTransactionIdAndLedgerMasterIdAndStatus(mSalesTranx.getId(), feesDetails.getFeeHead().getLedger().getId(), true);

                    if (ledgerTransactionDetails == null) {
//
                        ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType, feesDetails.getFeeHead().getLedger().getAssociateGroups(), mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);

                    } else {
                        try {
                            /**** New Postings Logic *****/
                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(feesDetails.getFeeHead().getLedger().getId(), 17L, mSalesTranx.getId(), true);
                            if (mLedger != null) {
                                mLedger.setAmount(fees);
                                mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                mLedger.setOperations("Updated");
                                ledgerTransactionPostingsRepository.save(mLedger);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                        }
                    }

                    /******* Insert into Sales Invoice Details against Fees Head *****/
                    TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
                    invoiceDetails.setAmount(fees);
                    if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
                    invoiceDetails.setOutlet(mSalesTranx.getOutlet());
                    if (mSalesTranx.getFiscalYear() != null) invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
                    invoiceDetails.setTranxSalesInvoice(mSalesTranx);
                    invoiceDetails.setFeeHead(feesDetails.getFeeHead());
                    invoiceDetails.setStatus(true);
                    try {
                        tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
                    } catch (Exception ex) {
                        studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
                    }
                } else if (feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("mts") && studentAdmission.getIsMts()) { // if student applicable of Scholarship
                    double fees = 0;
                    if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("male")) {
                        fees = feesDetails.getAmountForBoy();
                    } else if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("female")) {
                        fees = feesDetails.getAmountForGirl();
                    } else {
                        fees = feesDetails.getAmount();
                    }

                    LedgerTransactionPostings ledgerTransactionDetails = ledgerTransactionPostingsRepository.findByTransactionIdAndLedgerMasterIdAndStatus(mSalesTranx.getId(), feesDetails.getFeeHead().getLedger().getId(), true);

                    if (ledgerTransactionDetails == null) {
//
                        ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType, feesDetails.getFeeHead().getLedger().getAssociateGroups(), mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);

                    } else {
                        try {
                            //  ledgerTransactionDetailsRepository.ledgerPostingEdit(feesDetails.getFeeHead().getLedger().getId(), mSalesTranx.getId(), 17L, "CR", fees);
                            /**** New Postings Logic *****/
                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(feesDetails.getFeeHead().getLedger().getId(), 17L, mSalesTranx.getId(), true);
                            if (mLedger != null) {
                                mLedger.setAmount(fees);
                                mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                mLedger.setOperations("Updated");
                                ledgerTransactionPostingsRepository.save(mLedger);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                        }
                    }

                    /******* Insert into Sales Invoice Details against Fees Head *****/
                    TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
                    invoiceDetails.setAmount(fees);
                    if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
                    invoiceDetails.setOutlet(mSalesTranx.getOutlet());
                    if (mSalesTranx.getFiscalYear() != null) invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
                    invoiceDetails.setTranxSalesInvoice(mSalesTranx);
                    invoiceDetails.setFeeHead(feesDetails.getFeeHead());
                    invoiceDetails.setStatus(true);
                    try {
                        tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
                    } catch (Exception ex) {
                        studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
                    }
                } else if (feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("foundation") && studentAdmission.getIsFoundation()) { // if student applicable of Scholarship
                    double fees = 0;
                    if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("male")) {
                        fees = feesDetails.getAmountForBoy();
                    } else if (feesMaster.getStudentType() == 2 && (feesMaster.getStandard().getStandardName().equalsIgnoreCase("11") || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12")) && studentRegister.getGender().equalsIgnoreCase("female")) {
                        fees = feesDetails.getAmountForGirl();
                    } else {
                        fees = feesDetails.getAmount();
                    }

                    LedgerTransactionPostings ledgerTransactionDetails = ledgerTransactionPostingsRepository.findByTransactionIdAndLedgerMasterIdAndStatus(mSalesTranx.getId(), feesDetails.getFeeHead().getLedger().getId(), true);

                    if (ledgerTransactionDetails == null) {
//
                        ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType, feesDetails.getFeeHead().getLedger().getAssociateGroups(), mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);

                    } else {
                        try {
                            //  ledgerTransactionDetailsRepository.ledgerPostingEdit(feesDetails.getFeeHead().getLedger().getId(), mSalesTranx.getId(), 17L, "CR", fees);
                            /**** New Postings Logic *****/
                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(feesDetails.getFeeHead().getLedger().getId(), 17L, mSalesTranx.getId(), true);
                            if (mLedger != null) {
                                mLedger.setAmount(fees);
                                mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                mLedger.setOperations("Updated");
                                ledgerTransactionPostingsRepository.save(mLedger);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                        }
                    }

                    /******* Insert into Sales Invoice Details against Fees Head *****/
                    TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
                    invoiceDetails.setAmount(fees);
                    if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
                    invoiceDetails.setOutlet(mSalesTranx.getOutlet());
                    if (mSalesTranx.getFiscalYear() != null) invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
                    invoiceDetails.setTranxSalesInvoice(mSalesTranx);
                    invoiceDetails.setFeeHead(feesDetails.getFeeHead());
                    invoiceDetails.setStatus(true);
                    try {
                        tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
                    } catch (Exception ex) {
                        studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
                    }
                } else if (feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("bus")) { // if student applicable of bus
                    StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                            studentRegister.getId(), feesMaster.getAcademicYear().getId(), true);
                    if (studentTransport != null) {
                        double fees = studentTransport.getBus().getBusFee();

                        LedgerTransactionPostings ledgerTransactionDetails = ledgerTransactionPostingsRepository.findByTransactionIdAndLedgerMasterIdAndStatus(
                                mSalesTranx.getId(), feesDetails.getFeeHead().getLedger().getId(), true);

                        if (ledgerTransactionDetails == null) {
                            ledgerCommonPostings.callToPostings(fees, feesDetails.getFeeHead().getLedger(), tranxType,
                                    feesDetails.getFeeHead().getLedger().getAssociateGroups(), mSalesTranx.getFiscalYear(),
                                    mSalesTranx.getBranch(), mSalesTranx.getOutlet(), mSalesTranx.getBillDate(), mSalesTranx.getId(),
                                    mSalesTranx.getSalesInvoiceNo(), "CR", true, tranxType.getTransactionName(), key);
                        } else {
                            try {
                                //  ledgerTransactionDetailsRepository.ledgerPostingEdit(feesDetails.getFeeHead().getLedger().getId(), mSalesTranx.getId(), 17L, "CR", fees);
                                /**** New Postings Logic *****/
                                LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                        feesDetails.getFeeHead().getLedger().getId(), 17L, mSalesTranx.getId(), true);
                                if (mLedger != null) {
                                    mLedger.setAmount(fees);
                                    mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                    mLedger.setOperations("Updated");
                                    ledgerTransactionPostingsRepository.save(mLedger);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                            }
                        }

                        /******* Insert into Sales Invoice Details against Fees Head *****/
                        TranxSalesInvoiceDetails invoiceDetails = new TranxSalesInvoiceDetails();
                        invoiceDetails.setAmount(fees);
                        if (mSalesTranx.getBranch() != null) invoiceDetails.setBranch(mSalesTranx.getBranch());
                        invoiceDetails.setOutlet(mSalesTranx.getOutlet());
                        if (mSalesTranx.getFiscalYear() != null)
                            invoiceDetails.setFiscalYear(mSalesTranx.getFiscalYear());
                        invoiceDetails.setTranxSalesInvoice(mSalesTranx);
                        invoiceDetails.setFeeHead(feesDetails.getFeeHead());
                        invoiceDetails.setStatus(true);
                        try {
                            tranxSalesInvoiceDetailsRepository.save(invoiceDetails);
                        } catch (Exception ex) {
                            studentLogger.error("Exception in insertIntoTranxDetailFA ->" + ex.getMessage());
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in fee heads posting " + e.getMessage());
            studentLogger.error("Exception in insertIntoTranxDetailFA ->" + e.getMessage());
        }

    }/* End of Posting into Sales Accounts */

    public Object findStudents(HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            String id = request.getParameter("id");
            String firstName = request.getParameter("firstName");
            String middleName = request.getParameter("middleName");
            String lastName = request.getParameter("lastName");

            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            List<StudentRegister> studentRegister = new ArrayList<>();

            String query = "SELECT * FROM student_register_tbl WHERE outlet_id='" + users.getOutlet().getId()
                    + "' AND branch_id='" +users.getBranch().getId() + "' ";

            if (!firstName.equalsIgnoreCase("")) {
                query += " AND first_name='" + firstName + "' ";
            }
            if (!middleName.equalsIgnoreCase("")) {
                query += " AND middle_name='" + middleName + "' ";
            }
            if (!lastName.equalsIgnoreCase("")) {
                query += " AND last_name='" + lastName + "' ";
            }
            if (!id.equalsIgnoreCase("")) {
                query += " AND id!='" + id + "' ";
            }

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<< query >>>>>>>>>>>>>>>>>>>>>>>>" + query);
            Query q = entityManager.createNativeQuery(query, StudentRegister.class);
            studentRegister = q.getResultList();
            System.out.println("Limit total rows " + studentRegister.size());

            if (studentRegister.size() > 0) {
                responseMessage.setMessage("Already Registered");
                responseMessage.setResponseStatus(HttpStatus.OK.value());
            } else {
                responseMessage.setMessage("Data Not found");
                responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception" + e.getMessage());
            responseMessage.setResponse("Data Not found");
            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return responseMessage;
    }


    public JsonObject getStudentListAsPerStandard(HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        JsonArray res = new JsonArray();
        JsonObject result = new JsonObject();

        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));

            Long branchId = Long.valueOf(request.getParameter("branchId"));
            Long standardId = Long.valueOf(request.getParameter("standardId"));
            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
            Long divisionId = Long.valueOf((request.getParameter("divisionId")));
            Long studentType = Long.valueOf(request.getParameter("studentType"));

            List<FeesTransactionSummary> list = feesTransactionSummaryRepository.findByOutletIdAndBranchIdAndAcademicYearIdAndStandardIdAndDivisionIdForOutstanding(
                    users.getOutlet().getId(), branchId, academicYearId, standardId, divisionId, studentType);
            if (list != null) {
                for (FeesTransactionSummary feesTransactionSummary : list) {
                    JsonObject jObject = new JsonObject();
                    if (feesTransactionSummary.getBalance() > 0) {
                        jObject.addProperty("totalFees", feesTransactionSummary.getTotalFees());
                        jObject.addProperty("outstanding", feesTransactionSummary.getBalance());
                        jObject.addProperty("firstName", feesTransactionSummary.getStudentRegister().getFirstName());
                        jObject.addProperty("fatherName", feesTransactionSummary.getStudentRegister().getFatherName());
                        jObject.addProperty("lastName", feesTransactionSummary.getStudentRegister().getLastName());
                        jObject.addProperty("academicYearId", feesTransactionSummary.getAcademicYear().getId());
                        jObject.addProperty("standardId", feesTransactionSummary.getAcademicYear().getId());
                        jObject.addProperty("divisionId", feesTransactionSummary.getDivision().getId());

                        if (feesTransactionSummary.getStudentType() == 1) {
                            jObject.addProperty("studentType", "Day Scholar");
                        } else if (feesTransactionSummary.getStudentType() == 2) {
                            jObject.addProperty("studentType", "Residential");
                        }
                        jObject.addProperty("studentName", feesTransactionSummary.getStudentRegister().getFirstName() + " " + feesTransactionSummary.getStudentRegister().getMiddleName() + " " + feesTransactionSummary.getStudentRegister().getLastName());
                        jObject.addProperty("standard", feesTransactionSummary.getStandard().getStandardName());
                        jObject.addProperty("division", feesTransactionSummary.getDivision().getDivisionName());
                        jObject.addProperty("academicyear", feesTransactionSummary.getAcademicYear().getYear());
                        jObject.addProperty("branch", feesTransactionSummary.getBranch().getBranchName());
                        jObject.addProperty("mobileNo", feesTransactionSummary.getStudentRegister().getMobileNo() != null ? feesTransactionSummary.getStudentRegister().getMobileNo().toString() : "");
                        jObject.addProperty("paidAmount", feesTransactionSummary.getPaidAmount());
                        jObject.addProperty("studentId", feesTransactionSummary.getStudentRegister().getStudentId());
                        jObject.addProperty("id", feesTransactionSummary.getId());
                        res.add(jObject);
                    }


                }
                result.add("response", res);
                result.addProperty("responseStatus", HttpStatus.OK.value());


            } else {
                result.addProperty("message", "Failed to Load Data");
                result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty("message", "Failed to Load Data");
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


        return result;

    }


    public JsonObject getStudentListForPromotion(HttpServletRequest request) {
        JsonObject result = new JsonObject();
        JsonArray res = new JsonArray();
        LocalDate crdate = LocalDate.now();
        FiscalYear fiscalYear = generateFiscalYear.getFiscalYear(crdate);
        Boolean flag = false;

        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
            Long standardId = Long.valueOf(request.getParameter("standardId"));
            Long divisionId = Long.valueOf(request.getParameter("divisionId"));
            Integer studentType = Integer.valueOf(request.getParameter("studentType"));
            Long id = users.getOutlet().getId();
//            long fiscyearId= Long.parseLong(request.getHeader("academic-year-id"));
            Long isFeeClear = Long.parseLong(request.getParameter("isFeeClear"));
            AcademicYear academicYear = academicYearRepository.findByBranchIdAndFiscalYearIdAndStatus(users.getBranch().getId(), fiscalYear.getId(), true);
            if (academicYear != null) {
                flag = true;

            }
            List<Object[]> stList = null;
            if (isFeeClear == 1) {

                stList = studentRegisterRepository.getStudentListByOutletIdAndBranchIdAndAcademicYearIdAndStandardIdAndDivisionIdAndStudentTypeAndStatus(id, users.getBranch().getId(), academicYearId, standardId, divisionId, studentType, true);
            } else {
                stList = studentRegisterRepository.getStudentListhavingOutstanding(id, users.getBranch().getId(), academicYearId, standardId, divisionId, studentType, true);
            }
            System.out.println("stList size " + stList.size());
            for (int i = 0; i < stList.size(); i++) {
                Object[] obj = stList.get(i);
                System.out.println("obj " + obj[2]);
                JsonObject response = new JsonObject();
                Long studId = Long.valueOf(obj[0].toString());
//                StudentAdmission studentAdmission = studentAdmissionRepository.findTop1ByStudentRegisterIdAndStatusOrderByIdDesc(Long.valueOf(obj[0].toString()), true);
                StudentAdmission studentAdmission = studentAdmissionRepository.findTop1ByStudentRegisterIdAndIsRightOffAndStatusOrderByIdDesc(Long.valueOf(obj[0].toString()), false, true);


                if (studentAdmission != null && studentAdmission.getStandard().getId() == standardId) {
                    System.out.println("obj[7] " + obj[7].toString());
                    if ((int) obj[7] == 1) {
                        response.addProperty("studentType", "Day Scholar");
                    } else if ((int) obj[7] == 2) {
                        response.addProperty("studentType", "Residential");
                    }
                    FeesTransactionSummary feesTransactionSummary = feesTransactionSummaryRepository.findByStudentRegisterIdAndAcademicYearIdAndStandardIdAndDivisionIdAndStatus(Long.valueOf(obj[0].toString()), academicYearId, standardId, divisionId, true);
                    response.addProperty("pendingfees", feesTransactionSummary.getBalance() > 0 ? feesTransactionSummary.getBalance() : 0.0);
                    response.addProperty("standardName", obj[8].toString());
                    response.addProperty("academicYear", obj[9].toString());
                    response.addProperty("id", obj[0].toString());
                    response.addProperty("firstName", obj[1].toString());
                    response.addProperty("lastName", obj[2].toString());
                    response.addProperty("gender", obj[3].toString());
                    response.addProperty("mobileNo", obj[4].toString());
                    response.addProperty("birthDate", obj[5].toString() != null ? obj[5].toString() : "");
                    response.addProperty("dateOfAdmission", obj[6].toString());
                    response.addProperty("divisionId", obj[10].toString());
                    if (flag) {
                        response.addProperty("start_date_of_admission", fiscalYear.getDateStart().toString());
                        response.addProperty("nextAcademicYearId", academicYear.getId());
                    }
                    res.add(response);


                }
            }
            result.add("responseObject", res);
            result.addProperty("responseStatus", HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty("message", "Failed to Load Data!");
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;
    }

    public JsonObject getStudentListforBusTransport(HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        JsonArray res = new JsonArray();
        JsonObject result = new JsonObject();

        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            Long id = users.getOutlet().getId();
            Long branchId = Long.valueOf(request.getParameter("branchId"));
//            Long standardId = Long.valueOf(request.getParameter("standardId"));
            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
            Integer studentType = Integer.valueOf(request.getParameter("studentType"));

            List<Object[]> list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdANDStudentTypeAndStatusAndNsList(
                    id, branchId, academicYearId, studentType, true);
            System.out.println("list size " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = list.get(i);

                JsonObject response = new JsonObject();
//                StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(Long.parseLong(obj[0].toString()), true);
                if (academicYearId == 0 && studentType == 0) {
                    System.out.println("studentId =>" + obj[0].toString());
                    String studAdmissionData = studentAdmissionRepository.getLastAcademicData(Long.parseLong(obj[0].toString()));
                    System.out.println("studAdmissionData" + studAdmissionData);
                    String[] stdData = studAdmissionData.split(",");

                    System.out.println("stdData" + stdData.length);

                    if (!stdData[2].equalsIgnoreCase("") && Integer.parseInt(stdData[2]) == 1) {
                        response.addProperty("studentType", "Day Scholar");
                    } else if (!stdData[2].equalsIgnoreCase("") && Integer.parseInt(stdData[2]) == 2) {
                        response.addProperty("studentType", "Residential");
                    } else {
                        response.addProperty("studentType", "");
                    }

                    response.addProperty("academicYear", stdData[0] != null ? stdData[0] : "");
                    response.addProperty("standardName", stdData[1] != null ? stdData[1] : "");
                } else {
                    if ((int) obj[7] == 1) {
                        response.addProperty("studentType", "Day Scholar");
                    } else if ((int) obj[7] == 2) {
                        response.addProperty("studentType", "Residential");
                    } else {
                        response.addProperty("studentType", "");
                    }

                    response.addProperty("standardName", obj[8].toString());
                    response.addProperty("academicYear", obj[9].toString());
                }
                response.addProperty("id", obj[0].toString());
                response.addProperty("firstName", obj[1].toString());
                response.addProperty("lastName", obj[2].toString());
                response.addProperty("studentName", obj[1].toString() + " " + obj[2].toString());
                response.addProperty("gender", obj[3].toString());
                response.addProperty("mobileNo", obj[4].toString());
                response.addProperty("birthDate", obj[5].toString() != null ? obj[5].toString() : "");
                response.addProperty("dateOfAdmission", obj[6].toString());

//                response.addProperty("standardName", obj[8].toString());
//                response.addProperty("academicYear", obj[9].toString());
                res.add(response);

            }
            result.add("responseObject", res);
            result.addProperty("responseStatus", HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty(",message", "Failed to load Data");
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;
    }

    public JsonObject deleteStudent(HttpServletRequest request) {
        JsonObject responseMessage = new JsonObject();
        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));

        try {
            Long id = Long.valueOf(request.getParameter("studentId"));
//            Long studentAdmissionId = Long.valueOf(request.getParameter("studentAdmissionId"));
            String operationType = request.getParameter("operationType");
            System.out.println("student id =>" + id);
            StudentRegHistory studentRegHistory = null;

            StudentRegister studentRegister = studentRegisterRepository.findById(id).get();
            if (studentRegister != null) {
                studentRegHistory = saveInHistory(studentRegister, users);

                List<FeesTransactionSummary> feesTransactionSummaryList = feesTransactionSummaryRepository.findByStudentRegisterId(studentRegister.getId());
                for (FeesTransactionSummary feesTransactionSummary : feesTransactionSummaryList) {
                    if (feesTransactionSummary != null) {
                        FeesTranxSummaryHistory feesTranxSummaryHistory = saveFeesTranxsummaryHistroy(feesTransactionSummary, users, operationType);
                        if (feesTranxSummaryHistory != null) {
                            studentRegisterRepository.deleteStudentFeesDetailsByFeesSummaryId(feesTransactionSummary.getId());
                        }
                    }
                }
                studentRegisterRepository.deleteStudentFeesSummaryByStudentId(studentRegister.getId());

                List<StudentTransport> studentTransportList = studentTransportRepository.findByStudentRegisterIdAndStatus(studentRegHistory.getStudentRegisterId(), true);
                for (StudentTransport transport : studentTransportList) {
                    if (transport != null) {
                        StudentTransportHistory studentTransportHistory = saveInStudentTransportHistory(transport, users, operationType);
                        studentTransportRepository.deleteTransportRecordById(id);
                    }
                }

                List<LedgerMaster> ledgerMasters = ledgerMasterRepository.findByStudentRegisterIdAndStatus(studentRegHistory.getStudentRegisterId(), true);
                for (LedgerMaster ledgerMaster : ledgerMasters) {

                    LedgerMasterHistory ledgerMasterHistory = saveLedgerMasterHistory(ledgerMaster, operationType, users);
                    List<LedgerTransactionDetails> ledgerTransactionDetails = ledgerTransactionDetailsRepository.findByLedgerMasterIdAndStatus(ledgerMaster.getId(), true);
                    if (ledgerTransactionDetails.size() > 0) {
                        for (LedgerTransactionDetails ledgerTransactionDetails1 : ledgerTransactionDetails) {
                            LedgerTranxDetailHistory ledgerTranxDetailHistory = saveLedgerTranxDetailHistory(ledgerTransactionDetails1, users, operationType);
                        }
                    } else {
                        System.out.println("ledgertrnax detail not found");
                    }
                    LedgerBalanceSummary lbalsummary = ledgerBalanceSummaryRepository.findByLedgerMasterIdAndStatus(ledgerMaster.getId(), true);

                    if (lbalsummary != null) {
                        LedgerBalanceSummaryHistory ledgerBalanceSummaryHistory = saveLedgerbalsummaryHistory(lbalsummary, users, operationType);
                    } else {
                        System.out.println("Ledger balance summary not Found");
                    }

                    List<TranxSalesInvoice> tranxSalesInvoices = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndStatus(ledgerMaster.getId(), true);
                    for (TranxSalesInvoice tranxSalesInvoice : tranxSalesInvoices) {
                        if (tranxSalesInvoice != null) {
                            SalesInvoiceHistory salesInvoiceHistory = saveSaleInvoicehistory(tranxSalesInvoice, users, operationType);

                            List<TranxSalesInvoiceDetails> tranxSalesInvoiceDetails = tranxSalesInvoiceDetailsRepository.findByTranxSalesInvoiceIdAndStatus(tranxSalesInvoice.getId(), true);
                            for (TranxSalesInvoiceDetails tranxSalesInvoiceDetails1 : tranxSalesInvoiceDetails) {
                                System.out.println("TRANX DET ID " + tranxSalesInvoiceDetails1.getId() + "LDID " + tranxSalesInvoiceDetails1.getFeeHead().getLedger().getId() + " TRID " + tranxSalesInvoice.getId() + " trdate " + tranxSalesInvoice.getBillDate() + " paidAmt " + tranxSalesInvoiceDetails1.getAmount() + " CR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                                /*OLD POSTING*/
//                                ledgerTransactionDetailsRepository.ledgerPostingEditTranxDate(tranxSalesInvoiceDetails1.getFeeHead().getLedger().getId(), tranxSalesInvoice.getId(), 17L, "CR", 0.0, tranxSalesInvoice.getBillDate());

                                SalesInvoiceDetailsHistory salesInvoiceDetailsHistory = new SalesInvoiceDetailsHistory();
                                salesInvoiceDetailsHistory.setAmount(tranxSalesInvoiceDetails1.getAmount());
                                salesInvoiceDetailsHistory.setTranxSalesInvoiceDetailsId(tranxSalesInvoiceDetails1.getId());
                                salesInvoiceDetailsHistory.setBranchId(tranxSalesInvoiceDetails1.getBranch().getId());
                                salesInvoiceDetailsHistory.setOutletId(tranxSalesInvoiceDetails1.getOutlet().getId());
                                salesInvoiceDetailsHistory.setOperationType(operationType);
                                salesInvoiceDetailsHistory.setCreatedBy(users.getId());
                                if (tranxSalesInvoiceDetails1.getFiscalYear() != null) {
                                    salesInvoiceDetailsHistory.setFiscalYearId(tranxSalesInvoiceDetails1.getFiscalYear().getId());
                                }
                                salesInvoiceDetailsHistory.setStatus(true);
                                if (tranxSalesInvoiceDetails1.getFeeHead() != null) {
                                    salesInvoiceDetailsHistory.setFee_head_id(tranxSalesInvoiceDetails1.getFeeHead().getId());
                                }
                                salesInvoiceDetailsHistoryRepository.save(salesInvoiceDetailsHistory);
                            }
                            ledgerTransactionDetailsRepository.updateToStatusZeroTranxLedgerDetailByJVIdAndTranxType(tranxSalesInvoice.getId(), 17L);

                            studentRegisterRepository.deleteSalesTranxDetailsBySalesInvoiceId(tranxSalesInvoice.getId());
                        } else {
                            System.out.println("sales invoice not found");
                        }
                    }
                    studentRegisterRepository.deleteLedgerBalanceDataByLedgerId(ledgerMaster.getId());
                    studentRegisterRepository.deleteLedgerPostingDataByLedgerId(ledgerMaster.getId());
                    studentRegisterRepository.deleteLedgerTransDataByLedgerId(ledgerMaster.getId());
                    studentRegisterRepository.deleteReceiptPartDataByLedgerId(ledgerMaster.getId());
                    TranxSalesInvoice salesinvId = tranxSalesInvoiceRepository.findBySundryDebtorsId(ledgerMaster.getId());
                    studentRegisterRepository.deleteSalesTransDataByLedgerId(ledgerMaster.getId());
                }

                List<StudentAdmission> studentAdmissions = studentAdmissionRepository.findByStudentRegisterIdAndStatus(id, true);
                for (StudentAdmission studentAdmission : studentAdmissions) {
                    if (studentAdmission != null) {
                        StudentAdmHistory studentAdmHistory = saveAdmHistory(studentAdmission, users, operationType);
                    }
                }
                if (studentRegister != null) {
                    studentRegisterRepository.deleteLedgerDataByStudentId(studentRegister.getId());
                    studentRegisterRepository.deleteStudentFromAdmission(studentRegister.getId());
                    studentRegisterRepository.deleteStudentTransportByStdId(studentRegister.getId());
                    studentRegisterRepository.deleteStudentDataById(studentRegister.getId());
                }
                responseMessage.addProperty("message", "Student data deleted successfully");
                responseMessage.addProperty("responseStatus", HttpStatus.OK.value());
            } else {
                responseMessage.addProperty("message", "Student not found, please connect to admin");
                responseMessage.addProperty("responseStatus", HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage.addProperty("message", "Failed to delete student");
            responseMessage.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }

    public JournalMasterHistory saveJournalMasterHistory(TranxJournalMaster tranxJournalMaster, String operationType, Users users) {
        JournalMasterHistory journalMasterHistory = new JournalMasterHistory();
        journalMasterHistory.setJournalSrNo(tranxJournalMaster.getJournalSrNo());
        journalMasterHistory.setJournalNo(tranxJournalMaster.getJournalNo());
        journalMasterHistory.setFinancialYear(tranxJournalMaster.getFinancialYear());
        journalMasterHistory.setFeeReceiptNo(tranxJournalMaster.getFeeReceiptNo());
        if (tranxJournalMaster.getFiscalYear() != null) {
            journalMasterHistory.setFiscalYearId(tranxJournalMaster.getFiscalYear().getId());
        }
        journalMasterHistory.setNarrations(tranxJournalMaster.getNarrations());
        journalMasterHistory.setBranchId(tranxJournalMaster.getBranch().getId());
        journalMasterHistory.setOutletId(tranxJournalMaster.getOutlet().getId());
        journalMasterHistory.setTranxJournalMasterId(tranxJournalMaster.getId());
        journalMasterHistory.setCreatedBy(users.getId());
        journalMasterHistory.setOperationType(operationType);
        journalMasterHistory.setStatus(true);
        journalMasterHistory.setTotalAmt(tranxJournalMaster.getTotalAmt());
        journalMasterHistory.setTranscationDate(tranxJournalMaster.getTranscationDate());
        JournalMasterHistory journalMasterHistory1 = journalMasterHistoryRepository.save(journalMasterHistory);
        return journalMasterHistory1;
    }

    public JournalDetailsHistory saveJournalDetailsHistory(TranxJournalDetails jrDetails, String operationType, Users users) {
        JournalDetailsHistory journalDetailsHistory = new JournalDetailsHistory();
        journalDetailsHistory.setJournalMasterId(jrDetails.getTranxJournalMaster().getId());
        journalDetailsHistory.setTranxJournalDetailsId(jrDetails.getId());
        journalDetailsHistory.setLedgerMasterId(jrDetails.getLedgerMaster().getId());
        journalDetailsHistory.setOperationType(operationType);
        journalDetailsHistory.setBranchId(jrDetails.getBranch().getId());
        journalDetailsHistory.setLedgerType(jrDetails.getLedgerType());
        journalDetailsHistory.setPaidAmount(jrDetails.getPaidAmount());
        journalDetailsHistory.setCreatedBy(users.getId());
        journalDetailsHistory.setStatus(true);
        journalDetailsHistory.setType(jrDetails.getType());
        journalDetailsHistory.setOutletId(jrDetails.getOutlet().getId());
        JournalDetailsHistory journalDetailsHistory1 = journalDetailsHistoryRepository.save(journalDetailsHistory);
        return journalDetailsHistory1;
    }

    private StudentTransportHistory saveInStudentTransportHistory(StudentTransport studentTransport, Users users, String operationType) {
        StudentTransportHistory studentTransportHistory = new StudentTransportHistory();
        studentTransportHistory.setStudentTransportId(studentTransport.getId());
        studentTransportHistory.setBranchId(studentTransport.getBranch().getId());
        studentTransportHistory.setOutletId(studentTransport.getOutlet().getId());
        studentTransportHistory.setStatus(true);
        studentTransportHistory.setStudentBusId(studentTransport.getBus().getId());
        studentTransportHistory.setOperationType(operationType);
        if (studentTransport.getStandard() != null) {
            studentTransportHistory.setStandardId(studentTransport.getStandard().getId());
        }
        studentTransportHistory.setAcademicYearId(studentTransport.getAcademicYear().getYear());
        studentTransportHistory.setStudentRegisterId(studentTransport.getStudentRegister().getId());
        studentTransportHistory.setCreatedBy(users.getId());
        studentTransportHistory.setStudentType(studentTransport.getStudentType());
        StudentTransportHistory studentTransportHistory1 = studentTransporHistoryRepository.save(studentTransportHistory);
        return studentTransportHistory1;
    }

    private SalesInvoiceHistory saveSaleInvoicehistory(TranxSalesInvoice tranxSalesInvoice, Users users, String operationType) {

        SalesInvoiceHistory salesInvoiceHistory = new SalesInvoiceHistory();
        salesInvoiceHistory.setBalance(tranxSalesInvoice.getBalance());
        salesInvoiceHistory.setBillDate(tranxSalesInvoice.getBillDate());
        salesInvoiceHistory.setNarration(tranxSalesInvoice.getNarration());
        salesInvoiceHistory.setSalesInvoiceNo(tranxSalesInvoice.getSalesInvoiceNo());
        salesInvoiceHistory.setOperations(tranxSalesInvoice.getOperations());
        salesInvoiceHistory.setBranchId(tranxSalesInvoice.getBranch().getId());
        salesInvoiceHistory.setOperationType(operationType);
        salesInvoiceHistory.setOutletId(tranxSalesInvoice.getOutlet().getId());
        salesInvoiceHistory.setCreatedBy(users.getId());
        if (tranxSalesInvoice.getFeesAccounts() != null) {
            salesInvoiceHistory.setFees_account_ledger_id(tranxSalesInvoice.getFeesAccounts().getId());

        }
        salesInvoiceHistory.setFinancialYear(tranxSalesInvoice.getFinancialYear());
        salesInvoiceHistory.setTranxSalesInvoiceId(tranxSalesInvoice.getId());
        salesInvoiceHistory.setReference(tranxSalesInvoice.getReference());
//        salesInvoiceHistory.setFiscal_year_id(tranxSalesInvoice.getFiscalYear().getFiscalYear());
        salesInvoiceHistory.setTotalsgst(tranxSalesInvoice.getTotalsgst());
        salesInvoiceHistory.setRoundOff(tranxSalesInvoice.getRoundOff());
        if (tranxSalesInvoice.getSalesDiscountLedger() != null) {
            salesInvoiceHistory.setSales_discount_ledger_id(tranxSalesInvoice.getSalesDiscountLedger().getId());

        }
        salesInvoiceHistory.setSalesDiscountAmount(tranxSalesInvoice.getSalesDiscountAmount());
        salesInvoiceHistory.setSundry_debtors_id(tranxSalesInvoice.getSundryDebtors().getId());
        salesInvoiceHistory.setSalesDiscountPer(tranxSalesInvoice.getSalesDiscountPer());
        salesInvoiceHistory.setSalesSerialNumber(tranxSalesInvoice.getSalesSerialNumber());
        salesInvoiceHistory.setStatus(true);
        salesInvoiceHistory.setTotalBaseAmount(tranxSalesInvoice.getTotalBaseAmount());
        salesInvoiceHistory.setTotalAmount(tranxSalesInvoice.getTotalAmount());
        salesInvoiceHistory.setTaxableAmount(tranxSalesInvoice.getTaxableAmount());

        SalesInvoiceHistory salesInvoiceHistory1 = salesInvoiceHistoryRepository.save(salesInvoiceHistory);
        return salesInvoiceHistory1;
    }

    private LedgerBalanceSummaryHistory saveLedgerbalsummaryHistory(LedgerBalanceSummary lbalsummary, Users users, String operationType) {
        LedgerBalanceSummaryHistory balanceSummaryHistory = new LedgerBalanceSummaryHistory();
        balanceSummaryHistory.setBalance(lbalsummary.getBalance());
        balanceSummaryHistory.setOperationType(operationType);
        balanceSummaryHistory.setLedgerBalanceSummaryId(lbalsummary.getId());
        balanceSummaryHistory.setCredit(lbalsummary.getCredit());
        if (lbalsummary.getAssociateGroups() != null) {
            balanceSummaryHistory.setAssociateGroupName(lbalsummary.getAssociateGroups().getAssociatesName());
        }
        if (lbalsummary.getPrinciples() != null) {
            balanceSummaryHistory.setPrinciplesName(lbalsummary.getPrinciples().getPrincipleName());
        }
        balanceSummaryHistory.setUnderPrefix(lbalsummary.getUnderPrefix());
        balanceSummaryHistory.setBranchId(lbalsummary.getBranch().getId());
        balanceSummaryHistory.setOutletId(lbalsummary.getOutlet().getId());
        balanceSummaryHistory.setClosingBal(lbalsummary.getClosingBal());
        balanceSummaryHistory.setOpeningBal(lbalsummary.getOpeningBal());
        balanceSummaryHistory.setDebit(lbalsummary.getDebit());
        if (lbalsummary.getPrincipleGroups() != null) {
            balanceSummaryHistory.setPrinciplesGroupName(lbalsummary.getPrincipleGroups().getGroupName());

        }
        if (lbalsummary.getFoundations() != null) {
            balanceSummaryHistory.setFoundationsName(lbalsummary.getFoundations().getFoundationName());

        }
        balanceSummaryHistory.setLedgerMasterName(lbalsummary.getLedgerMaster().getLedgerName());
        balanceSummaryHistory.setStatus(true);
        LedgerBalanceSummaryHistory ledgerBalanceSummaryHistory1 = ledgerBalanceSummaryHistoryRepository.save(balanceSummaryHistory);
        return balanceSummaryHistory;
    }

    private LedgerTranxDetailHistory saveLedgerTranxDetailHistory(LedgerTransactionDetails ledgerTransactionDetails, Users users, String operationType) {

        LedgerTranxDetailHistory ltrnxDetailHistory = new LedgerTranxDetailHistory();
        ltrnxDetailHistory.setFinancialYear(ledgerTransactionDetails.getFinancialYear());
        ltrnxDetailHistory.setLedgerTransactionDetailsId(ledgerTransactionDetails.getId());
        if (ledgerTransactionDetails.getAssociateGroups() != null) {
            ltrnxDetailHistory.setAssociateGroupName(ledgerTransactionDetails.getAssociateGroups().getAssociatesName());
        }
        if (ledgerTransactionDetails.getBalanceMethod() != null) {
            ltrnxDetailHistory.setBalancingMethodName(ledgerTransactionDetails.getBalanceMethod().getBalancingMethod());
        }
        ltrnxDetailHistory.setClosingBal(ledgerTransactionDetails.getClosingBal());
        ltrnxDetailHistory.setFoundationsName(ledgerTransactionDetails.getFoundations().getFoundationName());
        ltrnxDetailHistory.setCredit(ledgerTransactionDetails.getCredit());
        ltrnxDetailHistory.setDebit(ledgerTransactionDetails.getDebit());
        ltrnxDetailHistory.setOperationType(operationType);
        ltrnxDetailHistory.setOpeningBal(ledgerTransactionDetails.getOpeningBal());
        ltrnxDetailHistory.setUnderPrefix(ledgerTransactionDetails.getUnderPrefix());
        ltrnxDetailHistory.setTranxType(ledgerTransactionDetails.getTranxType());
        ltrnxDetailHistory.setPaymentDate(ledgerTransactionDetails.getPaymentDate());
        ltrnxDetailHistory.setPaymentStatus(ledgerTransactionDetails.getPaymentStatus());
        ltrnxDetailHistory.setTransactionTypeName(ledgerTransactionDetails.getTransactionType().getTransactionName());
        ltrnxDetailHistory.setPrinciplesGroupName(ledgerTransactionDetails.getPrinciples().getPrincipleName());
        ltrnxDetailHistory.setTransactionId(ledgerTransactionDetails.getTransactionId());
        ltrnxDetailHistory.setCreatedBy(users.getId());

        LedgerTranxDetailHistory ledgerTranxDetailHistory = ledgerTranxDetailsHistoryRepository.save(ltrnxDetailHistory);
        return ledgerTranxDetailHistory;

    }

    private FeesTranxSummaryHistory saveFeesTranxsummaryHistroy(FeesTransactionSummary feesTransactionSummary, Users users, String operationType) {
        FeesTranxSummaryHistory feesTranxSummaryHistory = new FeesTranxSummaryHistory();
        feesTranxSummaryHistory.setOperationType(operationType);
        feesTranxSummaryHistory.setAcademicYear(feesTransactionSummary.getAcademicYear().getYear());
        feesTranxSummaryHistory.setBalance(feesTransactionSummary.getBalance());
        feesTranxSummaryHistory.setFeesTransactionSummaryId(feesTransactionSummary.getId());
        feesTranxSummaryHistory.setStudentGroup(feesTransactionSummary.getStudentGroup());
        feesTranxSummaryHistory.setStudentType(feesTransactionSummary.getStudentType());
        feesTranxSummaryHistory.setTotalFees(feesTransactionSummary.getTotalFees());
        feesTranxSummaryHistory.setFeesMasterId(feesTransactionSummary.getFeesMaster().getId());
        feesTranxSummaryHistory.setStatus(true);
        feesTranxSummaryHistory.setBranchId(feesTransactionSummary.getBranch().getId());
        feesTranxSummaryHistory.setOutletId(feesTransactionSummary.getOutlet().getId());
        feesTranxSummaryHistory.setConcessionAmount(feesTransactionSummary.getConcessionAmount());
        feesTranxSummaryHistory.setTotalFees(feesTransactionSummary.getTotalFees());
        feesTranxSummaryHistory.setIsManual(feesTransactionSummary.getIsManual());
        feesTranxSummaryHistory.setCreatedBy(feesTransactionSummary.getCreatedBy());
        feesTranxSummaryHistory.setCreatedAt(feesTransactionSummary.getCreatedAt());
        feesTranxSummaryHistory.setUpdatedAt(LocalDateTime.now());
        feesTranxSummaryHistory.setUpdatedBy(users.getId());
        FeesTranxSummaryHistory feesTranxSummaryHistory1 = feesTranxSummaryHistoryRepository.save(feesTranxSummaryHistory);
        return feesTranxSummaryHistory1;

    }

    private StudentRegHistory saveInHistory(StudentRegister studentRegister, Users users) {
        StudentRegHistory studentRegHistory = new StudentRegHistory();
        JsonObject jsonObject = new JsonObject();
        studentRegHistory.setStudentRegisterId(studentRegister.getId());
        studentRegHistory.setAge(studentRegister.getAge());
        studentRegHistory.setGender(studentRegister.getGender());
        studentRegHistory.setGeneralConduct(studentRegister.getGeneralConduct());
        studentRegHistory.setAadharNo(studentRegister.getAadharNo());
        studentRegHistory.setAltMobileNo(studentRegister.getAltMobileNo());
        studentRegHistory.setBirthDate(studentRegister.getBirthDate());
        studentRegHistory.setBirthPlace(studentRegister.getBirthPlace());
        studentRegHistory.setBranchId(studentRegister.getBranch().getId());
        if (studentRegister.getCaste() != null) {

            studentRegHistory.setCaste(studentRegister.getCaste().getCasteName());
        }
        studentRegHistory.setCurrentAddress(studentRegister.getCurrentAddress());
        studentRegHistory.setOfficeAddress(studentRegister.getOfficeAddress());
        studentRegHistory.setDateOfAdmission(studentRegister.getDateOfAdmission());
        studentRegHistory.setDol(studentRegister.getDol());
        studentRegHistory.setStudentUniqueNo(studentRegister.getStudentUniqueNo());
        studentRegHistory.setOutletId(studentRegister.getOutlet().getId());
        studentRegHistory.setTypeOfStudent(studentRegister.getTypeOfStudent());
        studentRegHistory.setStdInWhichAndWhen(studentRegister.getStdInWhichAndWhen());
        studentRegHistory.setStatus(true);
        studentRegHistory.setSameAsCurrentAddress(studentRegister.getSameAsCurrentAddress());
        studentRegHistory.setEmailId(studentRegister.getEmailId());
        studentRegHistory.setStdInPreviousSchool(studentRegister.getStdInPreviousSchool());
        studentRegHistory.setFirstName(studentRegister.getFirstName());
        studentRegHistory.setMiddleName(studentRegister.getMiddleName());
        studentRegHistory.setFatherName(studentRegister.getFatherName());
        studentRegHistory.setLastName(studentRegister.getLastName());
        studentRegHistory.setFatherOccupation(studentRegister.getFatherOccupation());
        studentRegHistory.setMotherOccupation(studentRegister.getMotherOccupation());
        if (studentRegister.getMotherTongue() != null) {
            studentRegHistory.setMotherTongue(studentRegister.getMotherTongue().getName());
        }
        studentRegHistory.setGeneralRegisterNo(studentRegister.getGeneralRegisterNo());
        studentRegHistory.setConcessionAmount(studentRegister.getConcessionAmount());
        studentRegHistory.setCreatedBy(users.getId());
        studentRegHistory.setCreatedAt(LocalDateTime.now());


        StudentRegHistory studentRegHistory1 = studentRegHistoryRepository.save(studentRegHistory);
        return studentRegHistory1;

    }

    private StudentAdmHistory saveAdmHistory(StudentAdmission studentAdmission, Users users, String operationType) {
        StudentAdmHistory admHistory = new StudentAdmHistory();
        if (studentAdmission.getAcademicYear() != null) {
            admHistory.setAcademicYear(studentAdmission.getAcademicYear().getYear());
        }
        if (studentAdmission.getDivision() != null) {
            admHistory.setDivision(studentAdmission.getDivision().getDivisionName());
        }
        admHistory.setBranchId(studentAdmission.getBranch().getId());
        admHistory.setOutletId(studentAdmission.getOutlet().getId());
        if (studentAdmission.getNewAdmittedStandard() != null) {
            admHistory.setNew_admitted_standard(studentAdmission.getNewAdmittedStandard().getId());
        }
        admHistory.setBusConcessionAmount(studentAdmission.getBusConcessionAmount());
        admHistory.setDateOfAdmission(studentAdmission.getDateOfAdmission());
        admHistory.setOperationType(operationType);
        admHistory.setConcessionAmount(studentAdmission.getConcessionAmount());
        admHistory.setTypeOfStudent(studentAdmission.getTypeOfStudent());
        admHistory.setStudentAdmissionId(studentAdmission.getId());
        admHistory.setIsBusConcession(studentAdmission.getIsBusConcession());
        admHistory.setIsHostel(studentAdmission.getIsHostel());
        admHistory.setIsScholarship(studentAdmission.getIsScholarship());
        admHistory.setIsVacation(studentAdmission.getIsVacation());
        admHistory.setNts(studentAdmission.getNts() != null ? studentAdmission.getNts().toString() : null);
        admHistory.setStatus(true);
        admHistory.setStudentGroup(studentAdmission.getStudentGroup());
        admHistory.setRefundAmount(studentAdmission.getRefundAmount());
        admHistory.setPaidAmount(studentAdmission.getPaidAmount());
        admHistory.setOutstanding(studentAdmission.getOutstanding());
        admHistory.setCreatedBy(users.getId());
        admHistory.setCreatedAt(LocalDateTime.now());

        StudentAdmHistory studentAdmHistory1 = studentAdmHistoryRepository.save(admHistory);

        return studentAdmHistory1;
    }

    private LedgerMasterHistory saveLedgerMasterHistory(LedgerMaster ledgerMaster, String operationType, Users users) {
        LedgerMasterHistory ledgerMasterHistory = new LedgerMasterHistory();
        ledgerMasterHistory.setLedgerMasterId(ledgerMaster.getId());
        ledgerMasterHistory.setOperationType(operationType);
        ledgerMasterHistory.setAddress(ledgerMaster.getAddress());
        ledgerMasterHistory.setAccountNumber(ledgerMaster.getAccountNumber());
        ledgerMasterHistory.setLedgerName(ledgerMaster.getLedgerName());
        ledgerMasterHistory.setEmail(ledgerMaster.getEmail());
        ledgerMasterHistory.setApplicableFrom(ledgerMaster.getApplicableFrom());
        ledgerMasterHistory.setTaxType(ledgerMaster.getTaxType());
        ledgerMasterHistory.setCreatedBy(users.getId());
        ledgerMasterHistory.setStatus(true);
        ledgerMasterHistory.setTcsApplicableDate(ledgerMaster.getTcsApplicableDate());
        if (ledgerMaster.getBalancingMethod() != null) {
            ledgerMasterHistory.setBalancingMethodName(ledgerMaster.getBalancingMethod().getBalancingMethod());
        }
        if (ledgerMaster.getAssociateGroups() != null) {
            ledgerMasterHistory.setAssociateGroupName(ledgerMaster.getAssociateGroups().getAssociatesName());
        }
        ledgerMasterHistory.setTcs(ledgerMaster.getTcs());
        ledgerMasterHistory.setBankBranch(ledgerMaster.getBankBranch());
        ledgerMasterHistory.setCreditDays(ledgerMaster.getCreditDays());
        ledgerMasterHistory.setLedgerCode(ledgerMaster.getLedgerCode());
        ledgerMasterHistory.setBankName(ledgerMaster.getBankName());
        ledgerMasterHistory.setDateOfRegistration(ledgerMaster.getDateOfRegistration());
        ledgerMasterHistory.setUniqueCode(ledgerMaster.getUniqueCode());
        ledgerMasterHistory.setTaxable(ledgerMaster.getTaxable());
        ledgerMasterHistory.setBranchId(ledgerMaster.getBranch().getId());
        ledgerMasterHistory.setOutletId(ledgerMaster.getOutlet().getId());
        ledgerMasterHistory.setDistrict(ledgerMaster.getDistrict());
        ledgerMasterHistory.setFoodLicenseNo(ledgerMaster.getFoodLicenseNo());
        if (ledgerMaster.getFoundations() != null) {
            ledgerMasterHistory.setFoundationsName(ledgerMaster.getFoundations().getFoundationName());
        }
        ledgerMasterHistory.setGstin(ledgerMaster.getGstin());
        ledgerMasterHistory.setIfsc(ledgerMaster.getIfsc());
        ledgerMasterHistory.setMailingName(ledgerMaster.getMailingName());
        ledgerMasterHistory.setMobile(ledgerMaster.getMobile());
        ledgerMasterHistory.setTds(ledgerMaster.getTds());
        ledgerMasterHistory.setTdsApplicableDate(ledgerMaster.getTdsApplicableDate());
        LedgerMasterHistory ledgerMasterHistory1 = ledgerMasterHistoryRepository.save(ledgerMasterHistory);
        return ledgerMasterHistory1;
    }


    public Object createStudentPromotion(HttpServletRequest request) {
        JsonObject result = new JsonObject();
        ResponseMessage responseMessage = new ResponseMessage();
        String rightOffacademicYear = "";
        String rightOffStandardName = "";
        String rightOffNote = "";
        Double rightOffAmt = 0D;
        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        String JsonToStr = request.getParameter("studentlist");
        String promotionType = request.getParameter("promotionType");
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap.containsKey("rightOffAcademicYear"))
            rightOffacademicYear = request.getParameter("rightOffAcademicYear");
        if (paramMap.containsKey("rightOffStandard")) rightOffStandardName = request.getParameter("rightOffStandard");
        if (paramMap.containsKey("rightOffAmt")) rightOffAmt = Double.valueOf(request.getParameter("rightOffAmt"));
        if (paramMap.containsKey("rightOffNote")) rightOffNote = request.getParameter("rightOffNote");

//        JsonArray studentArray = new JsonParser().parse(JsonToStr).getAsJsonArray();
        String[] studentArray = JsonToStr.split(",");
        if (promotionType.equalsIgnoreCase("exceptionpromotion") || promotionType.equalsIgnoreCase("normalpromotion")) {
            try {
                for (int i = 0; i < studentArray.length; i++) {
                    Long id = Long.parseLong(studentArray[i]);
                    if (id != null) {
                        List<StudentAdmission> studentAdmissionList = new ArrayList<>();
                        StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(id, true);
                        if (studentRegister != null) {
                            if (request.getParameterMap().containsKey("academicYearId") && request.getParameterMap().containsKey("academicYearId")) {

//                            FeesTransactionSummary transactionSummary=feesTransactionSummaryRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(studentRegister.getId(),)

                                StudentAdmission studentAdmission = new StudentAdmission();
                                studentAdmission.setStatus(true);
                                studentAdmission.setOutlet(users.getOutlet());
                                studentAdmission.setBranch(studentRegister.getBranch());
                                studentAdmission.setStudentRegister(studentRegister);
                                studentAdmission.setCreatedAt(LocalDateTime.now());
                                studentAdmission.setCreatedBy(users.getId());

                                if (request.getParameterMap().containsKey("academicYearId")) {
                                    Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
                                    AcademicYear academicYear = academicYearRepository.findByIdAndStatus(academicYearId, true);
                                    studentAdmission.setAcademicYear(academicYear);
                                }

                                if (request.getParameterMap().containsKey("standardId")) {
                                    Long currentStandardId = Long.valueOf(request.getParameter("standardId"));
                                    Standard currentStandard = standardRepository.findByIdAndStatus(currentStandardId, true);
                                    studentAdmission.setStandard(currentStandard);
                                }

                                if (request.getParameterMap().containsKey("divisionId")) {
                                    Long divisionId = Long.valueOf(request.getParameter("divisionId"));
                                    Division division = divisionRepository.findByIdAndStatus(divisionId, true);
                                    studentAdmission.setDivision(division);
                                }
                                if (request.getParameterMap().containsKey("isVacation")) {

                                    studentAdmission.setIsVacation(Boolean.valueOf(request.getParameter("isVacation")));
                                } else {
                                    studentAdmission.setIsVacation(false);
                                }

                                if (request.getParameterMap().containsKey("studentType")) {
                                    studentAdmission.setStudentType(Integer.valueOf(request.getParameter("studentType")));
                                }
                                if (request.getParameterMap().containsKey("studentGroup")) {
                                    studentAdmission.setStudentGroup(Integer.valueOf(request.getParameter("studentGroup")));
                                }
                                if (request.getParameterMap().containsKey("doa")) {
                                    studentAdmission.setDateOfAdmission(LocalDate.parse(request.getParameter("doa")));
                                }
                                if (request.getParameterMap().containsKey("nts")) {
                                    studentAdmission.setNts(Boolean.valueOf(request.getParameter("nts")));
                                } else {
                                    studentAdmission.setNts(false);
                                }
                                if (request.getParameterMap().containsKey("mts")) {

                                    studentAdmission.setIsMts(Boolean.valueOf(request.getParameter("mts")));
                                } else {
                                    studentAdmission.setIsMts(false);
                                }

                                if (request.getParameterMap().containsKey("foundation")) {
                                    studentAdmission.setIsFoundation(Boolean.valueOf(request.getParameter("foundation")));
                                } else {
                                    studentAdmission.setIsFoundation(false);
                                }
                                if (request.getParameterMap().containsKey("isScholarship")) {
                                    studentAdmission.setIsScholarship(Boolean.valueOf(request.getParameter("isScholarship")));
                                } else {
                                    studentAdmission.setIsScholarship(false);
                                }
                                studentAdmission.setIsRightOff(false);
                                studentAdmission.setIsHostel(true);
                                studentAdmission.setIsBusConcession(false);
//                        studentAdmission.setIsHostel(Boolean.valueOf(request.getParameter("isHostel")));
//                        studentAdmission.setIsBusConcession(Boolean.valueOf(request.getParameter("isBusConcession")));
//
//                        studentAdmission.setBusConcessionAmount(0L);
//                        if (request.getParameterMap().containsKey("busConcessionAmount")) {
//                            studentAdmission.setBusConcessionAmount(Long.valueOf(request.getParameter("busConcessionAmount")));
//                        }
//                        studentAdmission.setIsScholarship(Boolean.valueOf(request.getParameter("isScholarship")));
//                        studentAdmission.setNts(request.getParameter("nts"));
//                        if (request.getParameterMap().containsKey("concession")) {
//                            studentAdmission.setConcessionAmount(Long.valueOf(request.getParameter("concession")));
//                        }

//                        studentAdmissionList.add(studentAdmission);
                                StudentAdmission savedStudentAdmission = studentAdmissionRepository.save(studentAdmission);

                                if (savedStudentAdmission != null) {

//                                FeesMaster feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStatus(savedStudentAdmission.getBranch().getId(), savedStudentAdmission.getStandard().getId(), savedStudentAdmission.getAcademicYear().getId(), savedStudentAdmission.getStudentType(), true);
                                    FeesMaster feesMaster = null;
                                    if (studentAdmission.getStudentGroup() != null)
                                        feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStudentGroupAndStatus(
                                                savedStudentAdmission.getBranch().getId(), savedStudentAdmission.getStandard().getId(),
                                                savedStudentAdmission.getAcademicYear().getId(), savedStudentAdmission.getStudentType(),
                                                savedStudentAdmission.getStudentGroup(), true);
                                    else
                                        feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStatus(
                                                savedStudentAdmission.getBranch().getId(), savedStudentAdmission.getStandard().getId(),
                                                savedStudentAdmission.getAcademicYear().getId(), savedStudentAdmission.getStudentType(), true);

                                    if (feesMaster != null) {

//                                LedgerMaster ledgerMaster = createLedgerForStudent(studentInfo, users);

                                        FeesTransactionSummary feesTransactionSummary = new FeesTransactionSummary();
                                        double outstanding = 0.0;
                                        if (savedStudentAdmission.getStudentType() == 2 &&
                                                (savedStudentAdmission.getStandard().getStandardName().equalsIgnoreCase("11")
                                                        || feesMaster.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                            if (savedStudentAdmission.getStudentRegister().getGender().equalsIgnoreCase("male")) {
                                                feesTransactionSummary.setBalance(feesMaster.getAmountForBoy());
                                                feesTransactionSummary.setTotalFees(feesMaster.getAmountForBoy());
                                                outstanding = feesMaster.getAmountForBoy();
                                            } else if (studentAdmission.getStudentRegister().getGender().equalsIgnoreCase("female")) {
                                                feesTransactionSummary.setBalance(feesMaster.getAmountForGirl());
                                                feesTransactionSummary.setTotalFees(feesMaster.getAmountForGirl());
                                                outstanding = feesMaster.getAmountForGirl();
                                            }
                                        } else {
                                            feesTransactionSummary.setBalance(feesMaster.getAmount());
                                            feesTransactionSummary.setTotalFees(feesMaster.getAmount());
                                            outstanding = feesMaster.getAmount();
                                        }

                                        double headFee = getBusHeadFee(studentRegister, feesMaster); // 12000
                                        outstanding = outstanding - headFee; // 71000 - 12000 = 59000
                                        double totalFees = outstanding;
                                        feesTransactionSummary.setTotalFees(totalFees);
                                        feesTransactionSummary.setBalance(outstanding);

                                        /**** Is student applicablevacation yes th en consider amount otherwise deduct amount from fees master ******/
                                        double vacationHeadFee = getVacationHeadFee(studentRegister, feesMaster); // 12000
                                        if (!savedStudentAdmission.getIsVacation()) { // student bus not applicable
                                            outstanding = outstanding - vacationHeadFee; // 71000 - 12000 = 59000
                                            totalFees = outstanding;
                                            feesTransactionSummary.setTotalFees(totalFees);
                                            feesTransactionSummary.setBalance(outstanding);
                                        }

                                        /**** Is student applicable Scholarship yes then consider amount otherwise deduct amount from fees Master ****/

                                        double scholarshipfee = getScholarshipFee(studentRegister, feesMaster); //5500
                                        if (!savedStudentAdmission.getIsScholarship()) {

                                            outstanding = outstanding - scholarshipfee;
                                            totalFees = outstanding;
                                            feesTransactionSummary.setTotalFees(totalFees);
                                            feesTransactionSummary.setBalance(outstanding);

                                        }
                                        double foundationfee = getFoundationFee(studentRegister, feesMaster);
                                        if (!savedStudentAdmission.getIsFoundation()) {
                                            outstanding = outstanding - foundationfee;
                                            totalFees = outstanding;
                                            feesTransactionSummary.setTotalFees(totalFees);
                                            feesTransactionSummary.setBalance(outstanding);
                                        }

                                        double ntsFee = getNtsFee(studentRegister, feesMaster);
                                        if (!savedStudentAdmission.getNts()) {
                                            outstanding = outstanding - ntsFee;
                                            totalFees = outstanding;
                                            feesTransactionSummary.setTotalFees(totalFees);
                                            feesTransactionSummary.setBalance(outstanding);
                                        }

                                        double mtsFee = getMtsFee(studentRegister, feesMaster);
                                        if (!savedStudentAdmission.getIsMts()) {
                                            outstanding = outstanding - mtsFee;
                                            totalFees = outstanding;
                                            feesTransactionSummary.setTotalFees(totalFees);
                                            feesTransactionSummary.setBalance(outstanding);
                                        }


                                        feesTransactionSummary.setPaidAmount(0.0);
                                        feesTransactionSummary.setStudentRegister(savedStudentAdmission.getStudentRegister());
                                        feesTransactionSummary.setStandard(savedStudentAdmission.getStandard());
                                        feesTransactionSummary.setDivision(savedStudentAdmission.getDivision());
                                        feesTransactionSummary.setFeesMaster(feesMaster);
                                        feesTransactionSummary.setAcademicYear(savedStudentAdmission.getAcademicYear());
                                        feesTransactionSummary.setStudentType(savedStudentAdmission.getStudentType());
                                        feesTransactionSummary.setBranch(savedStudentAdmission.getBranch());
                                        feesTransactionSummary.setOutlet(savedStudentAdmission.getOutlet());
                                        feesTransactionSummary.setStudentGroup(savedStudentAdmission.getStudentGroup());
                                        feesTransactionSummary.setCreatedBy(users.getId());
                                        feesTransactionSummary.setStatus(true);

                                        feesTransactionSummaryRepository.save(feesTransactionSummary);


//                                    /******** Get Hostel fee by get hostel receipt under heads amount ********/
                                        StudentRegister savedStudentInfo = studentRegisterRepository.findByIdAndStatus(id, true);
                                        if (savedStudentInfo != null) {


                                            double hostelFee = getHostelHeadFees(savedStudentInfo, feesMaster);
                                            double totalFeeExceptHostel = outstanding - hostelFee;
                                            LedgerMaster ledgerMaster = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(id, users.getBranch().getId(), true);
                                            createTranxSalesInvoice(savedStudentInfo, users, ledgerMaster.getId(), outstanding, feesMaster, totalFeeExceptHostel, savedStudentAdmission);
                                            if (hostelFee > 0) {
                                                LedgerMaster hostelStudentLedger = null;
                                                Branch hostelBranch = branchRepository.getHostelBranchRecord(users.getOutlet().getId(), true);
                                                if (hostelBranch != null) {
                                                    hostelStudentLedger = ledgerMasterRepository.findByStudentRegisterIdAndOutletIdAndBranchIdAndStatus(studentRegister.getId(), users.getOutlet().getId(), hostelBranch.getId(), true);
                                                    if (hostelStudentLedger != null) {
                                                        createTranxSalesInvoiceForHostel(hostelBranch, savedStudentInfo, users, hostelStudentLedger.getId(), hostelFee, feesMaster, savedStudentAdmission, 0.0);

//                                                    return responseMessage;
                                                    } else {
                                                        try {
                                                            System.out.println("hostelStudentLedger created if not exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                            hostelStudentLedger = createLedgerForStudentForHostel(hostelBranch, savedStudentInfo, users);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while hostelStudentLedger " + e);
                                                        }
                                                        try {
                                                            createTranxSalesInvoiceForHostel(hostelBranch, savedStudentInfo, users, hostelStudentLedger.getId(), hostelFee, feesMaster, savedStudentAdmission, 0.0);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while createTranxSalesInvoiceForHostel" + e);
                                                        }
                                                        System.out.println("salesInvoiceHostel created again >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                    }


                                                } else {
                                                    System.out.println("hostel branch not found");
                                                }


                                            } else {
                                                System.out.println("hostel is not allocated to student");
                                            }
                                        }


                                    }

                                }


                            }


                        }
                    }
                    responseMessage.setMessage("Student promoted successfully");
                    responseMessage.setResponseStatus(HttpStatus.OK.value());
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseMessage.setMessage("Student Promotion Failed !");
                responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
        } else {

            LocalDate crdate = LocalDate.now();
            String rightOff = "Right Off";
            FiscalYear fiscalYear = generateFiscalYear.getFiscalYear(crdate);
            AcademicYear academicYear = academicYearRepository.findByBranchIdAndYearAndStatus(users.getBranch().getId(), rightOffacademicYear, true);
            Standard standard = standardRepository.findByStandardNameAndBranchIdAndStatus(rightOffStandardName, users.getBranch().getId(), true);
            LedgerMaster rightoffledger = ledgerMasterRepository.findByLedgerNameIgnoreCaseAndOutletIdAndBranchIdAndStatus(rightOff, users.getOutlet().getId(), users.getBranch().getId(), true);

            /*** Inserting into Transaction Journal Master****/
            TransactionTypeMaster tranxType = transactionTypeMasterRepository.findByTransactionCodeIgnoreCase("JRNL");
            LedgerMaster mStudent = ledgerMasterRepository.findByStudentRegisterIdAndOutletIdAndBranchIdAndStatus(Long.valueOf(studentArray[0]), users.getOutlet().getId(), users.getBranch().getId(), true);

            /**** Journal Voucher For right off ledger  *****/
            System.out.println("rightOffAmt >>>>>>>>>>>>>>>>>>>>>>>" + rightOffAmt);
            System.out.println("rightoffyear>>>>>>>>>" + rightOffacademicYear);
            TranxJournalMaster journalMaster = new TranxJournalMaster();
            Branch branch = null;
            if (users.getBranch() != null) branch = users.getBranch();
            Outlet outlet = users.getOutlet();
            Long count = tranxJournalMasterRepository.findLastRecord(outlet.getId(), branch.getId());
            String serailNo = String.format("%05d", count + 1);// 5 digit serial number
            GenerateDates generateDates = new GenerateDates();
            String currentMonth = generateDates.getCurrentMonth().substring(0, 3);
            String jtCode = "JRNL" + currentMonth + serailNo;
            journalMaster.setOutlet(outlet);
            journalMaster.setBranch(branch);
            journalMaster.setTranscationDate(crdate);
            journalMaster.setJournalSrNo(count + 1);

            journalMaster.setJournalNo(jtCode);
//                        journalMaster.setTotalAmt(currentPaidAmount);
            journalMaster.setTotalAmt(rightOffAmt);
//                            journalMaster.setNarrations("Journal entry from student to DP Acc:" + mStudent.getId());
            journalMaster.setNarrations(rightOffNote);
            journalMaster.setCreatedBy(users.getId());
            journalMaster.setStatus(true);
            /* fiscal year mapping*/
            FiscalYear fiscalYears = generateFiscalYear.getFiscalYear(crdate);
            if (fiscalYears != null) {
                journalMaster.setFiscalYear(fiscalYears);
                journalMaster.setFinancialYear(fiscalYears.getFiscalYear());
            }
            TranxJournalMaster tranxJournalMaster = tranxJournalMasterRepository.save(journalMaster);
            try {
                /**** Tranx Journal Details for rightoff A/C *****/
                TranxJournalDetails tranxJournalDetailsforRightOff = new TranxJournalDetails();
                tranxJournalDetailsforRightOff.setBranch(branch);
                tranxJournalDetailsforRightOff.setOutlet(outlet);
                tranxJournalDetailsforRightOff.setStatus(true);

                tranxJournalDetailsforRightOff.setLedgerMaster(rightoffledger);
                tranxJournalDetailsforRightOff.setTranxJournalMaster(tranxJournalMaster);
                tranxJournalDetailsforRightOff.setType("DR");
                tranxJournalDetailsforRightOff.setLedgerType(rightoffledger.getSlugName());
                tranxJournalDetailsforRightOff.setCreatedBy(users.getId());
                tranxJournalDetailsforRightOff.setPaidAmount(rightOffAmt);
                tranxJournalDetailsforRightOff.setStatus(true);
                TranxJournalDetails journalDetails = tranxJournalDetailsRepository.save(tranxJournalDetailsforRightOff);
                ledgerCommonPostings.callToPostings(rightOffAmt, rightoffledger, tranxType, null, fiscalYear, users.getBranch(), users.getOutlet(), crdate, tranxJournalMaster.getId(), tranxJournalMaster.getJournalNo(), "DR", true, tranxType.getTransactionName(), "right-off");
                /******* Details for Student A/C ********/
                TranxJournalDetails tranxJournalDetailsforStudent = new TranxJournalDetails();
                tranxJournalDetailsforStudent.setBranch(branch);
                tranxJournalDetailsforStudent.setOutlet(outlet);
                tranxJournalDetailsforStudent.setStatus(true);

                tranxJournalDetailsforStudent.setLedgerMaster(mStudent);
                tranxJournalDetailsforStudent.setTranxJournalMaster(tranxJournalMaster);
                tranxJournalDetailsforStudent.setType("CR");
                tranxJournalDetailsforStudent.setLedgerType(mStudent.getSlugName());
                tranxJournalDetailsforStudent.setCreatedBy(users.getId());
                tranxJournalDetailsforStudent.setPaidAmount(rightOffAmt);
                tranxJournalDetailsforStudent.setStatus(true);
                TranxJournalDetails journalDetailss = tranxJournalDetailsRepository.save(tranxJournalDetailsforStudent);
                ledgerCommonPostings.callToPostings(rightOffAmt, mStudent, tranxType, null, fiscalYear, users.getBranch(), users.getOutlet(), crdate, tranxJournalMaster.getId(), tranxJournalMaster.getJournalNo(), "CR", true, tranxType.getTransactionName(), "right-off");
                StudentAdmission studentAdmission = studentAdmissionRepository.findTop1ByStudentRegisterIdAndStatusOrderByIdDesc(Long.valueOf(studentArray[0]), true);
                if (studentAdmission != null) {
                    studentAdmission.setIsRightOff(true);
                    studentAdmissionRepository.save(studentAdmission);
                }
                System.out.println("RightOff things---->" + rightOffStandardName + " " + rightOffacademicYear);
                RightOffStudent rightOffStudent = new RightOffStudent();
                rightOffStudent.setAcademicYear(rightOffacademicYear);
                rightOffStudent.setBranchId(users.getBranch().getId());
                rightOffStudent.setStudentName(studentAdmission.getStudentRegister().getFirstName() + " " + studentAdmission.getStudentRegister().getMiddleName() + " " + studentAdmission.getStudentRegister().getLastName());
                rightOffStudent.setOutletId(users.getOutlet().getId());
                rightOffStudent.setStandardName(rightOffStandardName);
                rightOffStudent.setStudentId(Long.valueOf(studentArray[0]));
                rightOffStudent.setRightOffAmt(rightOffAmt);
                rightOffStudent.setCreatedAt(LocalDateTime.now());
                rightOffStudent.setRightOffNote(rightOffNote);
                rightOffStudent.setStatus(true);
                rightOffStudent.setCreatedBy(users.getId());
                rightOffStudentRepository.save(rightOffStudent);
                System.out.println("student saved rightoff of table");


                responseMessage.setMessage("Student terminated successfully");
                responseMessage.setResponseStatus(HttpStatus.OK.value());

            } catch (Exception e) {
                responseMessage.setMessage("Student termination failed");
                responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                e.printStackTrace();
            }
        }


//
//            ledgerCommonPostings.callToPostings(rightOffAmt,rightoffledger,tranxType,null,fiscalYear,users.getBranch(),users.getOutlet(),
//                    crdate,null,null,"CR",true,tranxType.getTransactionName(),"right-off");


        return responseMessage;


    }

    public InputStream sheetLoad(Map<String, String> request, HttpServletRequest req) throws IOException {
        String[] Mainheader = {"BRANCH", "STANDARD"};
        String[] HEADERs = {"FULL NAME", "DOB", "MOBILE NO", "MOTHER NAME", "ADDRESS", "CASTE", "SUB CASTE", "CATEGORY", "AADHAR NO", "STUDENT TYPE", "STUDENT ID", "TYPE OF STUDENT"};


        String standardId = request.get("standardId");
        String standardName = "";
        if (!standardId.equalsIgnoreCase("")) {
            Standard standard = standardRepository.findByIdAndStatus(Long.parseLong(standardId), true);
            if (standard != null) {
                standardName = standard.getStandardName();
            }
        }

        Long branchId = Long.valueOf(request.get("branchId"));
        String branchName = "";
        Branch branch = branchRepository.findByIdAndStatus(branchId, true);
        if (branch != null) {
            branchName = branch.getBranchName();
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student List");
            Row headerRow = sheet.createRow(3);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            Row row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("BRANCH");
            row1.createCell(1).setCellValue(branchName);

            Row row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue("STANDARD");
            row2.createCell(1).setCellValue(standardName);


            String JsonTostr = request.get("studentList");
            JsonArray studentArray = new JsonParser().parse(JsonTostr).getAsJsonArray();
            rowIdx = 4;
            for (int i = 0; i < studentArray.size(); i++) {
                JsonObject studObj = studentArray.get(i).getAsJsonObject();
                Row row = sheet.createRow(rowIdx++);
                try {
                    row.createCell(0).setCellValue(studObj.get("studentName").getAsString());
                    row.createCell(1).setCellValue(!studObj.get("birthDate").isJsonNull() ? studObj.get("birthDate").getAsString() : "");
                    row.createCell(2).setCellValue(!studObj.get("mobileNo").isJsonNull() ? studObj.get("mobileNo").getAsString() : "");
                    row.createCell(3).setCellValue(!studObj.get("motherName").isJsonNull() ? studObj.get("motherName").getAsString() : "");
                    row.createCell(4).setCellValue(!studObj.get("address").isJsonNull() ? studObj.get("address").getAsString() : "");
                    row.createCell(5).setCellValue(!studObj.get("casteName").isJsonNull() ? studObj.get("casteName").getAsString() : "");
                    row.createCell(6).setCellValue(!studObj.get("subCasteName").isJsonNull() ? studObj.get("subCasteName").getAsString() : "");
                    row.createCell(7).setCellValue(!studObj.get("category").isJsonNull() ? studObj.get("category").getAsString() : "");
                    row.createCell(8).setCellValue(!studObj.get("aadharNo").isJsonNull() ? studObj.get("aadharNo").getAsString() : "");
                    row.createCell(9).setCellValue(!studObj.get("studentType").isJsonNull() ? studObj.get("studentType").getAsString() : "");
                    row.createCell(10).setCellValue(!studObj.get("studentId").isJsonNull() ? studObj.get("studentId").getAsString() : "");
                    row.createCell(11).setCellValue(!studObj.get("TypeofStudent").isJsonNull() ? studObj.get("TypeofStudent").getAsString() : "");

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception" + e.getMessage());
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException e ===>" + e.getMessage());
            throw new RuntimeException("failed to import Data to Excel File" + e.getMessage());
        }
    }

    public JsonObject getStudentDataforPromotions(HttpServletRequest request) {
        JsonObject response = new JsonObject();
        JsonObject result = new JsonObject();
        try {
            StudentAdmission sAdmission = studentAdmissionRepository.findByIdAndStatus(Long.valueOf(request.getParameter("studentAdmissionId")), true);

            if (sAdmission != null) {
                response.addProperty("studentAdmissionId", sAdmission.getId());
                response.addProperty("studentId", sAdmission.getStudentRegister().getId());
                response.addProperty("branchId", sAdmission.getBranch().getId());
                response.addProperty("outletId", sAdmission.getOutlet().getId());
                /*if (sAdmission.getDateOfAdmission() != null) {
                    response.addProperty("dateOfAdmission", String.valueOf(sAdmission.getDateOfAdmission()));
                } else {
                    if (sAdmission.getStudentRegister().getDateOfAdmission() != null) {
                        response.addProperty("dateOfAdmission", String.valueOf(sAdmission.getStudentRegister().getDateOfAdmission()));
                    } else {
                        response.addProperty("dateOfAdmission", "");
                    }
                }*/
                response.addProperty("dateOfAdmission", String.valueOf(sAdmission.getDateOfAdmission() != null ? sAdmission.getDateOfAdmission() : sAdmission.getStudentRegister().getDateOfAdmission() != null ? sAdmission.getStudentRegister().getDateOfAdmission() : ""));
                response.addProperty("admittedStandard", String.valueOf(sAdmission.getNewAdmittedStandard() != null ? sAdmission.getNewAdmittedStandard().getStandardName() : sAdmission.getStudentRegister().getAdmittedStandard() != null ? sAdmission.getStudentRegister().getAdmittedStandard().getStandardName() : ""));
                response.addProperty("firstName", sAdmission.getStudentRegister().getFirstName());
                response.addProperty("lastName", sAdmission.getStudentRegister().getLastName());
                response.addProperty("studentName", sAdmission.getStudentRegister().getFirstName() + " " + sAdmission.getStudentRegister().getLastName());
                response.addProperty("admittedStandardId", String.valueOf((sAdmission.getNewAdmittedStandard() != null ? sAdmission.getNewAdmittedStandard().getId() : sAdmission.getStudentRegister().getAdmittedStandard() != null ? sAdmission.getStudentRegister().getAdmittedStandard().getId() : "")));
                response.addProperty("studentIsOld", sAdmission.getTypeOfStudent() != null ? sAdmission.getTypeOfStudent() : sAdmission.getStudentRegister().getTypeOfStudent() != null ? sAdmission.getStudentRegister().getTypeOfStudent() : "");
                response.addProperty("academicYearId", sAdmission.getAcademicYear().getId());
                response.addProperty("academicYear", sAdmission.getAcademicYear().getYear());
                response.addProperty("currentStandardId", sAdmission.getStandard().getId());
                response.addProperty("currentStandardName", sAdmission.getStandard().getStandardName());
                response.addProperty("divisionId", String.valueOf(sAdmission.getDivision() != null ? sAdmission.getDivision().getId() : ""));
                response.addProperty("division", sAdmission.getDivision() != null ? sAdmission.getDivision().getDivisionName() : "");
                response.addProperty("studType", sAdmission.getStudentType());
                response.addProperty("studentGroup", String.valueOf(sAdmission.getStudentGroup() != null ? sAdmission.getStudentGroup() : ""));
                response.addProperty("isHostel", sAdmission.getIsHostel());
                response.addProperty("isBusConcession", sAdmission.getIsBusConcession());
                response.addProperty("isVacation", sAdmission.getIsVacation());
                response.addProperty("busConcessionAmount", sAdmission.getBusConcessionAmount());
                response.addProperty("isScholarship", sAdmission.getIsScholarship());

                response.addProperty("nts", sAdmission.getNts());
                response.addProperty("mts", sAdmission.getIsMts());
                response.addProperty("foundation", sAdmission.getIsFoundation());
//                response.addProperty("concession", sAdmission.getConcessionAmount());
                result.addProperty("message", "success");
                result.addProperty("responseStatus", HttpStatus.OK.value());
                result.add("data", response);
            } else {
                result.addProperty("message", "Data not found");
                result.addProperty("responseStatus", HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            studentLogger.error("getStudentDataforPromotions -> failed to get admission data " + e.getMessage());
            response.addProperty("message", "Failed to get student admission data");
            response.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;
    }

    public Object promoteStudent(HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        try {
            StudentAdmission studentAdm = studentAdmissionRepository.findByIdAndStatus(Long.valueOf(request.getParameter("studentAdmissionId")), true);

            String typeofStudent = request.getParameter("studentIsOld");
            long admissionId = Long.valueOf(request.getParameter("studentAdmissionId"));
            String academicYearId1 = request.getParameter("academicYearId");
            long currentStdId = Long.parseLong(request.getParameter("currentStandardId"));
            long studentRegisterId = Long.parseLong(request.getParameter("studentId"));

            List<StudentAdmission> stdsAdmission = studentAdmissionRepository.findStudentDuplication(
                    academicYearId1, typeofStudent, currentStdId, studentRegisterId, admissionId, users.getOutlet().getId(), users.getBranch().getId());

            System.out.println("stdsAdmission size=" + stdsAdmission.size());
            if (stdsAdmission.size() > 0) {
                System.out.println("duplication found");

                responseMessage.setMessage("Duplication Found");
                responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else {
                try {
                    StudentAdmission studentAdmission1 = studentAdmissionRepository.findByIdAndStatus(admissionId, true);
                    FeesTransactionSummary feesTransactionSummary = feesTransactionSummaryRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                            studentRegisterId, studentAdmission1.getAcademicYear().getId(), true);
                    /*if (feesTransactionSummary != null && feesTransactionSummary.getPaidAmount() > 0) {
                        System.out.println("Paid Amount->" + feesTransactionSummary.getPaidAmount());
                        System.out.println("First Delete PaymentDeails");
                        responseMessage.setMessage("Student already Paid Fees,Please Delete Fees Payment");
                        responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    } else */
                    {

                        if (studentAdmission1 != null) {
                            studentAdmission1.setStatus(true);
                            if (request.getParameterMap().containsKey("studentIsOld")) {
                                studentAdmission1.setTypeOfStudent(request.getParameter("studentIsOld"));
                            }
                            if (Integer.valueOf(request.getParameter("studentIsOld")) == 1) {
                                Long standardId = Long.valueOf(request.getParameter("admittedStandardId"));
                                Standard standard = standardRepository.findByIdAndStatus(standardId, true);
                                studentAdmission1.setNewAdmittedStandard(standard);
                                studentAdmission1.setTypeOfStudent(request.getParameter("studentIsOld"));
                            }
                            studentAdmission1.setOutlet(users.getOutlet());
                            studentAdmission1.setDateOfAdmission(LocalDate.parse(request.getParameter("doa")));
                            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
                            AcademicYear academicYear = academicYearRepository.findByIdAndStatus(academicYearId, true);
                            studentAdmission1.setAcademicYear(academicYear);

                            Long currentStandardId = Long.valueOf(request.getParameter("currentStandardId"));
                            Standard currentStandard = standardRepository.findByIdAndStatus(currentStandardId, true);
                            studentAdmission1.setStandard(currentStandard);

                            if (request.getParameterMap().containsKey("divisionId")) {
                                Long divisionId = Long.valueOf(request.getParameter("divisionId"));
                                Division division = divisionRepository.findByIdAndStatus(divisionId, true);
                                studentAdmission1.setDivision(division);
                            }
                            studentAdmission1.setStudentType(Integer.valueOf(request.getParameter("studentType")));
                            if (request.getParameterMap().containsKey("studentGroup")) {
                                studentAdmission1.setStudentGroup(Integer.valueOf(request.getParameter("studentGroup")));
                            } else {
                                studentAdmission1.setStudentGroup(null);
                            }

                            studentAdmission1.setIsHostel(false);
                            if (request.getParameterMap().containsKey("isHostel")) {
                                studentAdmission1.setIsHostel(Boolean.valueOf(request.getParameter("isHostel")));
                            }

                            studentAdmission1.setIsBusConcession(false);
                            if (request.getParameterMap().containsKey("isBusConcession")) {
                                studentAdmission1.setIsBusConcession(Boolean.valueOf(request.getParameter("isBusConcession")));
                            }

                            studentAdmission1.setIsVacation(false);
                            if (request.getParameterMap().containsKey("isVacation")) {
                                studentAdmission1.setIsVacation(Boolean.valueOf(request.getParameter("isVacation")));
                            }

                            studentAdmission1.setBusConcessionAmount(0L);
                            if (request.getParameterMap().containsKey("busConcessionAmount")) {
                                studentAdmission1.setBusConcessionAmount(Long.valueOf(request.getParameter("busConcessionAmount")));
                            }
                            studentAdmission1.setIsScholarship(false);
                            if (request.getParameterMap().containsKey("isScholarship")) {
                                studentAdmission1.setIsScholarship(Boolean.valueOf(request.getParameter("isScholarship")));
                            }

                            studentAdmission1.setNts(false);
                            if (request.getParameterMap().containsKey("nts")) {
                                studentAdmission1.setNts(Boolean.valueOf(request.getParameter("nts")));
                            }
                            studentAdmission1.setIsMts(false);
                            if (request.getParameterMap().containsKey("mts")) {
                                studentAdmission1.setIsMts(Boolean.valueOf(request.getParameter("mts")));
                            }
                            studentAdmission1.setIsFoundation(false);
                            if (request.getParameterMap().containsKey("foundation")) {
                                studentAdmission1.setIsFoundation(Boolean.valueOf(request.getParameter("foundation")));
                            }
                            if (request.getParameterMap().containsKey("concession")) {
                                studentAdmission1.setConcessionAmount(Long.valueOf(request.getParameter("concession")));
                            }

                            StudentAdmission studentAdmission2 = admissionRepository.save(studentAdmission1);

                            if (studentAdmission2 != null) {
                                FeesMaster oldFeesMaster = null;
                                FeesMaster feesMaster = null;
                                if (studentAdmission2.getStudentGroup() != null)
                                    feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStudentGroupAndStatus(studentAdmission2.getStudentRegister().getBranch().getId(), studentAdmission2.getStandard().getId(), studentAdmission2.getAcademicYear().getId(), studentAdmission2.getStudentType(), studentAdmission2.getStudentGroup(), true);
                                else
                                    feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStatus(studentAdmission2.getStudentRegister().getBranch().getId(), studentAdmission2.getStandard().getId(), studentAdmission2.getAcademicYear().getId(), studentAdmission2.getStudentType(), true);
                                if (feesMaster != null) {

                                    double outstanding = 0.0;
                                    double totalFees = 0;
                                    FeesTransactionSummary feesTransactionSummary1 = feesTransactionSummaryRepository.
                                            findByStudentRegisterIdAndAcademicYearIdAndStatus(studentRegisterId,
                                                    studentAdmission2.getAcademicYear().getId(), true);
                                    if (feesTransactionSummary1 != null) {
                                        oldFeesMaster = feesTransactionSummary1.getFeesMaster();
                                        if (studentAdmission2.getStudentType() == 2 && (studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("11")
                                                || studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                            if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("male")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForBoy());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForBoy());
                                                totalFees = feesMaster.getAmountForBoy();
                                            } else if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("female")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForGirl());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForGirl());
                                                totalFees = feesMaster.getAmountForGirl();
                                            }
                                        } else {
                                            totalFees = feesMaster.getAmount();
                                        }
                                        System.out.println("totalFees >>>>>>>>>>>>>>" + totalFees);
                                        double headFee = getBusHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("BUS headFee >>>>>>>>>>>>>>" + headFee);
                                        totalFees = totalFees - headFee; // 71000 - 12000 = 59000
                                        System.out.println("totalFees >>>>>>>>>>>>>>" + totalFees);
                                        Double busfees = 0.0;
                                        StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                                                studentRegisterId, studentAdmission2.getAcademicYear().getId(), true);
                                        if (studentTransport != null) {
                                            busfees = studentTransport.getBus().getBusFee();
                                            totalFees = totalFees + busfees;
                                        }

                                        double vacationHeadFee = getVacationHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("vacationHeadFee >>>>>>>>>>>>>>" + vacationHeadFee);
                                        if (!studentAdmission2.getIsVacation()) { // student vacation not applicable
                                            totalFees = totalFees - vacationHeadFee; // 71000 - 12000 = 59000
                                        }
                                        double scholarshipfee = getScholarshipFee(studentAdmission2.getStudentRegister(), feesMaster); //5500
                                        if (studentAdmission2.getIsScholarship() != null && !studentAdmission2.getIsScholarship()) {
                                            totalFees = totalFees - scholarshipfee;
                                        }
                                        double foundationfee = getFoundationFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsFoundation() != null && !studentAdmission2.getIsFoundation()) {
                                            totalFees = totalFees - foundationfee;
                                        }

                                        double ntsFee = getNtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getNts() != null && !studentAdmission2.getNts()) {
                                            totalFees = totalFees - ntsFee;
                                        }

                                        double mtsFee = getMtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsMts() != null && !studentAdmission2.getIsMts()) {
                                            totalFees = totalFees - mtsFee;
                                        }

                                        feesTransactionSummary1.setTotalFees(totalFees);
                                        if (feesTransactionSummary1.getPaidAmount() > 0 && feesTransactionSummary1.getConcessionAmount() > 0) {

                                            outstanding = totalFees - feesTransactionSummary1.getPaidAmount();
                                            outstanding = outstanding - feesTransactionSummary1.getConcessionAmount();
                                        } else if (feesTransactionSummary1.getPaidAmount() > 0 && feesTransactionSummary1.getConcessionAmount() == 0) {
                                            outstanding = totalFees - feesTransactionSummary1.getPaidAmount();
                                        }else{
                                            outstanding = totalFees;
                                        }
                                        feesTransactionSummary1.setBalance(outstanding);
                                        feesTransactionSummary1.setStudentRegister(studentAdmission2.getStudentRegister());
                                        feesTransactionSummary1.setStandard(studentAdmission2.getStandard());
                                        feesTransactionSummary1.setDivision(studentAdmission2.getDivision());
                                        feesTransactionSummary1.setFeesMaster(feesMaster);
                                        feesTransactionSummary1.setAcademicYear(studentAdmission2.getAcademicYear());
                                        feesTransactionSummary1.setStudentType(studentAdmission2.getStudentType());
                                        feesTransactionSummary1.setBranch(studentAdmission2.getBranch());
                                        feesTransactionSummary1.setOutlet(studentAdmission2.getOutlet());
                                        feesTransactionSummary1.setStudentGroup(studentAdmission2.getStudentGroup());
                                        feesTransactionSummary1.setCreatedBy(users.getId());
                                        feesTransactionSummary1.setStatus(true);

                                        try {
                                            feesTransactionSummaryRepository.save(feesTransactionSummary1);
                                            System.out.println("Student Feestransaction Saved");
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                            responseMessage.setMessage("Failed to save FeeTranscation");
                                        }
//                                        StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndStatus(studentRegisterId, true);
//                                        if (studentTransport != null) {
//                                            System.out.println("Student transport exists.........");
//                                            studentRegisterRepository.deleteStudentBusId(studentTransport.getId());
//                                        } else {
//                                            System.out.println("Student bus not exists.........");
//                                        }

                                        LedgerMaster ledgerMaster = ledgerMasterRepository.findByStudentRegisterIdAndOutletIdAndBranchIdAndStatus(
                                                studentRegisterId, studentAdmission2.getOutlet().getId(), studentAdmission2.getBranch().getId(), true);

                                        FiscalYear fiscalYear = getFiscalYearFromAcademicYear(studentAdmission2.getAcademicYear().getYear());
//                                        double hostelFee = 0;
                                        if (ledgerMaster != null) {
                                            /******** Get Hostel fee by get hostel receipt under heads amount ********/
                                            double hostelFee = getHostelHeadFees(studentAdmission2.getStudentRegister(), feesMaster);
                                            System.out.println("hostelFee........." + hostelFee);
                                            double totalFeeExceptHostel = feesTransactionSummary1.getTotalFees() - hostelFee;
//                                            if(feesTransactionSummary1.getPaidAmount()>0)
//                                            {
                                            totalFeeExceptHostel = feesTransactionSummary1.getTotalFees() - hostelFee;
//                                            }else {
//                                                 totalFeeExceptHostel = outstanding - hostelFee;
//                                            }
                                            System.out.println("totalFeeExceptHostel........." + totalFeeExceptHostel);
                                            Double sumCR = 0.0;
                                            Double sumDR = 0.0;
                                            Double outstandingBal = 0.0;
                                            sumCR = ledgerTransactionPostingsRepository.findsumCR(ledgerMaster.getId());//-0.20 //0
                                            System.out.println("SumCR:" + sumCR);
                                            sumDR = ledgerTransactionPostingsRepository.findsumDR(ledgerMaster.getId());//-0.40
                                            System.out.println("SumDR:" + sumDR);
                                            TranxSalesInvoice tranxSalesInvoice = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(
                                                    ledgerMaster.getId(), fiscalYear.getId(), true);
                                            if (tranxSalesInvoice != null) {
                                                if (sumCR > 0) {
//                                                    outstandingBal = (totalFeeExceptHostel - sumCR) + totalFees - sumCR;//0-(-0.40)-0.20  0+41500)+0=41500
                                                    outstandingBal = totalFeeExceptHostel - sumCR;

                                                } else {
                                                    outstandingBal = totalFeeExceptHostel;
                                                }
                                                tranxSalesInvoice.setBillDate(studentAdmission2.getDateOfAdmission() != null ? studentAdmission2.getDateOfAdmission() : tranxSalesInvoice.getBillDate());
                                                tranxSalesInvoice.setTotalAmount(totalFeeExceptHostel);
                                                tranxSalesInvoice.setTotalBaseAmount(totalFeeExceptHostel);
                                                tranxSalesInvoice.setBalance(totalFeeExceptHostel);
                                                tranxSalesInvoice.setUpdatedAt(LocalDateTime.now());
                                                tranxSalesInvoice.setUpdatedBy(users.getId());
                                                tranxSalesInvoice.setStatus(true);
                                                tranxSalesInvoiceRepository.save(tranxSalesInvoice);
                                                System.out.println("tranxSalesInvoice.getId() " + tranxSalesInvoice.getId());
                                                tranxSalesInvoiceDetailsRepository.MakeStatusZero(tranxSalesInvoice.getId(), false);
                                            }

                                            try {
                                                /**** New Postings Logic for Sundry Debtors *****/
                                                LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.
                                                        findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                                ledgerMaster.getId(), 17L, tranxSalesInvoice.getId(), true);
                                                if (mLedger != null) {
                                                    mLedger.setAmount(totalFeeExceptHostel);
                                                    mLedger.setTransactionDate(tranxSalesInvoice.getBillDate());
                                                    mLedger.setOperations("Updated");
                                                    ledgerTransactionPostingsRepository.save(mLedger);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                studentLogger.error("Exception => while saving ledgerPostingEdit in student promote" + e);
                                            }

                                            List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(oldFeesMaster.getId(), true);

                                            for (FeesDetails feesDetails : feesDetailsList) {
                                                System.out.println("Head ledger id->" + feesDetails.getFeeHead().getLedger().getId()
                                                        + " tranxSalesInvoice.getId()->"+tranxSalesInvoice.getId());
                                                /**** New Postings Logic : Edit*****/
                                                LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                        feesDetails.getFeeHead().getLedger().getId(), 17L, tranxSalesInvoice.getId(), true);
                                                if (mLedger != null) {
                                                    mLedger.setAmount(0.0);
                                                    mLedger.setTransactionDate(tranxSalesInvoice.getBillDate());
                                                    mLedger.setOperations("Updated");
                                                    mLedger.setStatus(false);
                                                    ledgerTransactionPostingsRepository.save(mLedger);
                                                } else {
                                                    System.out.println("Fee head ledger not Exist");
                                                }
                                            }

                                            TransactionTypeMaster tranxType = transactionTypeMasterRepository.findByTransactionCodeIgnoreCase("feestr");
                                            insertIntoTranxDetailFA(studentAdmission2.getStudentRegister(), tranxSalesInvoice, tranxType, feesMaster, "create"); // for Fees Accounts : cr


                                            Branch hostelBranch = branchRepository.getHostelBranchRecord(users.getOutlet().getId(), true);
                                            LedgerMaster hostelStudentLedger = null;
                                            if (hostelBranch != null) {
                                                hostelStudentLedger = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(studentRegisterId, hostelBranch.getId(), true);
                                                if (hostelStudentLedger != null) {
                                                    System.out.println("Already student ledger exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                    TranxSalesInvoice salesInvoiceHostel = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(
                                                            hostelStudentLedger.getId(), fiscalYear.getId(), true);

                                                    if (salesInvoiceHostel != null) {
                                                        System.out.println("Already salesInvoiceHostel exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                        salesInvoiceHostel.setTotalAmount(0.0);
                                                        salesInvoiceHostel.setTotalBaseAmount(0.0);
                                                        salesInvoiceHostel.setBalance(0.0);
                                                        salesInvoiceHostel.setUpdatedAt(LocalDateTime.now());
                                                        salesInvoiceHostel.setUpdatedBy(users.getId());
                                                        salesInvoiceHostel.setStatus(false);
                                                        tranxSalesInvoiceRepository.save(salesInvoiceHostel);

                                                        System.out.println("tranxSalesInvoice.getId() " + salesInvoiceHostel.getId());
                                                        tranxSalesInvoiceDetailsRepository.MakeStatusZero(salesInvoiceHostel.getId(), false);
                                                    }
                                                    if (hostelStudentLedger != null) {

                                                        try {
                                                            /**** New Postings Logic : Edit*****/
                                                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                                    hostelStudentLedger.getId(), 17L, salesInvoiceHostel.getId(), true);
                                                            if (mLedger != null) {
                                                                mLedger.setAmount(0.0);
                                                                mLedger.setTransactionDate(salesInvoiceHostel.getBillDate());
                                                                mLedger.setOperations("Updated");
                                                                mLedger.setStatus(false);
                                                                ledgerTransactionPostingsRepository.save(mLedger);
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                                                        }
//
                                                    } else {
                                                        System.out.println("student hostel not found");
                                                    }

                                                    /******** Get Hostel fee by searching hostel name ********/

//
                                                    /******** Get Hostel fee by get hostel receipt under heads amount ********/
                                                    List<FeesDetails> hostelfeesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatusAndHostelsOnly(oldFeesMaster.getId(), false, true);
                                                    for (FeesDetails feesDetails : hostelfeesDetailsList) {
                                                        double fees = 0;
                                                        System.out.println("feesDetails.getFeeHead().getLedger().getId()->" + feesDetails.getFeeHead().getLedger().getId());

                                                        try {

                                                            /**** New Postings Logic : Edit*****/
                                                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                                    feesDetails.getFeeHead().getLedger().getId(), 17L, salesInvoiceHostel.getId(), true);
                                                            if (mLedger != null) {
                                                                mLedger.setAmount(0.0);
                                                                mLedger.setTransactionDate(tranxSalesInvoice.getBillDate());
                                                                mLedger.setOperations("Updated");
                                                                mLedger.setStatus(false);
                                                                ledgerTransactionPostingsRepository.save(mLedger);
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                                                        }

                                                    }

                                                }

                                                System.out.println("hostelFee =>" + hostelFee);
                                                if (hostelFee > 0) {
                                                    if (hostelStudentLedger == null) {
                                                        try {
                                                            System.out.println("hostelStudentLedger created if not exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                            hostelStudentLedger = createLedgerForStudentForHostel(hostelBranch, studentAdmission2.getStudentRegister(), users);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while hostelStudentLedger " + e);
                                                        }
                                                    }

                                                    try {
                                                        Double sumCR1 = 0.0;
                                                        Double sumDR1 = 0.0;
                                                        Double outstandingBal1 = 0.0;
                                                        sumCR1 = ledgerTransactionPostingsRepository.findsumCR(hostelStudentLedger.getId());//-0.20 //0
                                                        sumDR1 = ledgerTransactionPostingsRepository.findsumDR(hostelStudentLedger.getId());//-0.40
                                                        //outstandingBal1 = sumDR1 - sumCR1;//0-(-0.40)-0.20  0+41500)+0=41500
                                                        outstandingBal1 = (sumDR1 - sumCR1) + sumCR1;
                                                        createTranxSalesInvoiceForHostel(hostelBranch, studentAdmission2.getStudentRegister(),
                                                                users, hostelStudentLedger.getId(), hostelFee, feesMaster, studentAdmission1, 0.0);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        studentLogger.error("Exception => while createTranxSalesInvoiceForHostel" + e);
                                                    }
                                                    System.out.println("salesInvoiceHostel created again >>>>>>>>>>>>>>>>>>>>>>>>>>>");

                                                } else {

                                                    System.out.println("Hostel is not allocated to Student");
                                                }
                                            }
                                        } else {
                                            System.out.println("Ledger Not Found By Student Id in Ledger Master");
                                            responseMessage.setMessage("Ledger Not Found By Student Id in Ledger Master");
                                            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                                        }
                                    } else {
                                        System.out.println("Fees Transaction summary new created");
                                        studentLogger.error("Fees Transaction summary new created fro the student =" + studentAdmission2.getStudentRegister().getId());
                                        feesTransactionSummary1 = new FeesTransactionSummary();
                                        if (studentAdmission2.getStudentType() == 2 && (studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("11")
                                                || studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                            if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("male")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForBoy());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForBoy());

                                                outstanding = feesMaster.getAmountForBoy();
                                            } else if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("female")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForGirl());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForGirl());

                                                outstanding = feesMaster.getAmountForGirl();
                                            }
                                        } else {
                                            feesTransactionSummary1.setTotalFees(0.0);
                                            feesTransactionSummary1.setTotalFees(feesMaster.getAmount());

                                            outstanding = feesMaster.getAmount();
                                        }
                                        System.out.println("outstanding >>>>>>>>>>>>>>" + outstanding);
                                        double headFee = getBusHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("BUS headFee >>>>>>>>>>>>>>" + headFee);
                                        outstanding = outstanding - headFee; // 71000 - 12000 = 59000
                                        System.out.println("outstanding >>>>>>>>>>>>>>" + outstanding);
                                        Double busfees = 0.0;
                                        StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                                                studentRegisterId, studentAdmission2.getAcademicYear().getId(), true);
                                        if (studentTransport != null) {
                                            busfees = studentTransport.getBus().getBusFee();
                                            outstanding = outstanding + busfees;
                                        }

                                        double vacationHeadFee = getVacationHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("vacationHeadFee >>>>>>>>>>>>>>" + vacationHeadFee);
                                        if (!studentAdmission2.getIsVacation()) { // student vacation not applicable
                                            outstanding = outstanding - vacationHeadFee; // 71000 - 12000 = 59000
                                        }
                                        double scholarshipfee = getScholarshipFee(studentAdmission2.getStudentRegister(), feesMaster); //5500
                                        if (studentAdmission2.getIsScholarship() != null && !studentAdmission2.getIsScholarship()) {
                                            outstanding = outstanding - scholarshipfee;
                                        }
                                        double foundationfee = getFoundationFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsFoundation() != null && !studentAdmission2.getIsFoundation()) {
                                            outstanding = outstanding - foundationfee;
                                        }

                                        double ntsFee = getNtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getNts() != null && !studentAdmission2.getNts()) {
                                            outstanding = outstanding - ntsFee;
                                        }

                                        double mtsFee = getMtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsMts() != null && !studentAdmission2.getIsMts()) {
                                            outstanding = outstanding - mtsFee;
                                        }
                                        feesTransactionSummary1.setPaidAmount(0.0);
                                        feesTransactionSummary1.setTotalFees(totalFees);
                                        feesTransactionSummary1.setBalance(outstanding);
                                        feesTransactionSummary1.setStudentRegister(studentAdmission2.getStudentRegister());
                                        feesTransactionSummary1.setStandard(studentAdmission2.getStandard());
                                        feesTransactionSummary1.setDivision(studentAdmission2.getDivision());
                                        feesTransactionSummary1.setFeesMaster(feesMaster);
                                        feesTransactionSummary1.setAcademicYear(studentAdmission2.getAcademicYear());
                                        feesTransactionSummary1.setStudentType(studentAdmission2.getStudentType());
                                        feesTransactionSummary1.setBranch(studentAdmission2.getBranch());
                                        feesTransactionSummary1.setOutlet(studentAdmission2.getOutlet());
                                        feesTransactionSummary1.setStudentGroup(studentAdmission2.getStudentGroup());
                                        feesTransactionSummary1.setCreatedBy(users.getId());
                                        feesTransactionSummary1.setStatus(true);

                                        try {
                                            feesTransactionSummaryRepository.save(feesTransactionSummary1);
                                            System.out.println("Student Feestransaction Saved");
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                            responseMessage.setMessage("Failed to save FeeTranscation");
                                        }

                                        FiscalYear fiscalYear = getFiscalYearFromAcademicYear(studentAdmission2.getAcademicYear().getYear());

                                        double hostelFee = getHostelHeadFees(studentAdmission2.getStudentRegister(), feesMaster);
                                        double totalFeeExceptHostel = outstanding - hostelFee;
                                        LedgerMaster ledgerMaster = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(
                                                studentRegisterId, users.getBranch().getId(), true);
                                        createTranxSalesInvoice(studentAdmission2.getStudentRegister(), users, ledgerMaster.getId(),
                                                outstanding, feesMaster, totalFeeExceptHostel, studentAdmission2);
                                        if (hostelFee > 0) {
                                            LedgerMaster hostelStudentLedger = null;
                                            Branch hostelBranch = branchRepository.getHostelBranchRecord(users.getOutlet().getId(), true);
                                            if (hostelBranch != null) {
                                                hostelStudentLedger = ledgerMasterRepository.
                                                        findByStudentRegisterIdAndOutletIdAndBranchIdAndStatus(
                                                                studentRegisterId, users.getOutlet().getId(), hostelBranch.getId(), true);
                                                if (hostelStudentLedger != null) {
                                                    createTranxSalesInvoiceForHostel(hostelBranch, studentAdmission2.getStudentRegister(),
                                                            users, hostelStudentLedger.getId(), hostelFee, feesMaster, studentAdmission2, 0.0);
                                                } else {
                                                    try {
                                                        System.out.println("hostelStudentLedger created if not exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                        hostelStudentLedger = createLedgerForStudentForHostel(hostelBranch,
                                                                studentAdmission2.getStudentRegister(), users);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        studentLogger.error("Exception => while hostelStudentLedger " + e);
                                                    }
                                                    try {
                                                        createTranxSalesInvoiceForHostel(hostelBranch,  studentAdmission2.getStudentRegister(),
                                                                users, hostelStudentLedger.getId(), hostelFee, feesMaster,
                                                                studentAdmission2, 0.0);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        studentLogger.error("Exception => while createTranxSalesInvoiceForHostel" + e);
                                                    }
                                                    System.out.println("salesInvoiceHostel created again >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                }


                                            } else {
                                                System.out.println("hostel branch not found");
                                            }


                                        } else {
                                            System.out.println("hostel is not allocated to student");
                                        }
                                    }

                                }
                            }
                            responseMessage.setMessage("Student promoted Successfully !");
                            responseMessage.setResponseStatus(HttpStatus.OK.value());
                        } else {
                            responseMessage.setMessage("Student admission record not found");
                            responseMessage.setResponseStatus(HttpStatus.NOT_FOUND.value());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    studentLogger.error("Exception  promoteStudent =>" + e);

                    responseMessage.setMessage("Failed to update student admission");
                    responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                return responseMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            studentLogger.error("Exception promoteStudent =>" + e);

            responseMessage.setMessage("Failed to update student admission");
            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }


    public Object promoteStudentBkp_26102023(HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        try {
            StudentAdmission studentAdm = studentAdmissionRepository.findByIdAndStatus(Long.valueOf(request.getParameter("studentAdmissionId")), true);

            String typeofStudent = request.getParameter("studentIsOld");
            long admissionId = Long.valueOf(request.getParameter("studentAdmissionId"));
            String academicYearId1 = request.getParameter("academicYearId");
            long currentStdId = Long.parseLong(request.getParameter("currentStandardId"));
            long studentRegisterId = Long.parseLong(request.getParameter("studentId"));

            List<StudentAdmission> stdsAdmission = studentAdmissionRepository.findStudentDuplication(
                    academicYearId1, typeofStudent, currentStdId, studentRegisterId, admissionId, users.getOutlet().getId(), users.getBranch().getId());

            System.out.println("stdsAdmission size=" + stdsAdmission.size());
            if (stdsAdmission.size() > 0) {
                System.out.println("duplication found");

                responseMessage.setMessage("Duplication Found");
                responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else {
                try {
                    StudentAdmission studentAdmission1 = studentAdmissionRepository.findByIdAndStatus(admissionId, true);
                    FeesTransactionSummary feesTransactionSummary = feesTransactionSummaryRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                            studentRegisterId, studentAdmission1.getAcademicYear().getId(), true);
                    if (feesTransactionSummary != null && feesTransactionSummary.getPaidAmount() > 0) {
                        System.out.println("Paid Amount->" + feesTransactionSummary.getPaidAmount());
                        System.out.println("First Delete PaymentDeails");
                        responseMessage.setMessage("Student already Paid Fees,Please Delete Fees Payment");
                        responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    } else {

                        if (studentAdmission1 != null) {
                            studentAdmission1.setStatus(true);
                            if (request.getParameterMap().containsKey("studentIsOld")) {
                                studentAdmission1.setTypeOfStudent(request.getParameter("studentIsOld"));
                            }
                            if (Integer.valueOf(request.getParameter("studentIsOld")) == 1) {
                                Long standardId = Long.valueOf(request.getParameter("admittedStandardId"));
                                Standard standard = standardRepository.findByIdAndStatus(standardId, true);
                                studentAdmission1.setNewAdmittedStandard(standard);
                                studentAdmission1.setTypeOfStudent(request.getParameter("studentIsOld"));
                            }
                            studentAdmission1.setOutlet(users.getOutlet());
                            studentAdmission1.setDateOfAdmission(LocalDate.parse(request.getParameter("doa")));
                            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));
                            AcademicYear academicYear = academicYearRepository.findByIdAndStatus(academicYearId, true);
                            studentAdmission1.setAcademicYear(academicYear);

                            Long currentStandardId = Long.valueOf(request.getParameter("currentStandardId"));
                            Standard currentStandard = standardRepository.findByIdAndStatus(currentStandardId, true);
                            studentAdmission1.setStandard(currentStandard);

                            if (request.getParameterMap().containsKey("divisionId")) {
                                Long divisionId = Long.valueOf(request.getParameter("divisionId"));
                                Division division = divisionRepository.findByIdAndStatus(divisionId, true);
                                studentAdmission1.setDivision(division);
                            }
                            studentAdmission1.setStudentType(Integer.valueOf(request.getParameter("studentType")));
                            if (request.getParameterMap().containsKey("studentGroup")) {
                                studentAdmission1.setStudentGroup(Integer.valueOf(request.getParameter("studentGroup")));
                            } else {
                                studentAdmission1.setStudentGroup(null);
                            }

                            studentAdmission1.setIsHostel(false);
                            if (request.getParameterMap().containsKey("isHostel")) {
                                studentAdmission1.setIsHostel(Boolean.valueOf(request.getParameter("isHostel")));
                            }

                            studentAdmission1.setIsBusConcession(false);
                            if (request.getParameterMap().containsKey("isBusConcession")) {
                                studentAdmission1.setIsBusConcession(Boolean.valueOf(request.getParameter("isBusConcession")));
                            }

                            studentAdmission1.setIsVacation(false);
                            if (request.getParameterMap().containsKey("isVacation")) {
                                studentAdmission1.setIsVacation(Boolean.valueOf(request.getParameter("isVacation")));
                            }

                            studentAdmission1.setBusConcessionAmount(0L);
                            if (request.getParameterMap().containsKey("busConcessionAmount")) {
                                studentAdmission1.setBusConcessionAmount(Long.valueOf(request.getParameter("busConcessionAmount")));
                            }
                            studentAdmission1.setIsScholarship(false);
                            if (request.getParameterMap().containsKey("isScholarship")) {
                                studentAdmission1.setIsScholarship(Boolean.valueOf(request.getParameter("isScholarship")));
                            }

                            studentAdmission1.setNts(false);
                            if (request.getParameterMap().containsKey("nts")) {
                                studentAdmission1.setNts(Boolean.valueOf(request.getParameter("nts")));
                            }
                            studentAdmission1.setIsMts(false);
                            if (request.getParameterMap().containsKey("mts")) {
                                studentAdmission1.setIsMts(Boolean.valueOf(request.getParameter("mts")));
                            }
                            studentAdmission1.setIsFoundation(false);
                            if (request.getParameterMap().containsKey("foundation")) {
                                studentAdmission1.setIsFoundation(Boolean.valueOf(request.getParameter("foundation")));
                            }
                            if (request.getParameterMap().containsKey("concession")) {
                                studentAdmission1.setConcessionAmount(Long.valueOf(request.getParameter("concession")));
                            }

                            StudentAdmission studentAdmission2 = admissionRepository.save(studentAdmission1);

                            if (studentAdmission2 != null) {
                                FeesMaster oldFeesMaster = null;
                                FeesMaster feesMaster = null;
                                if (studentAdmission2.getStudentGroup() != null)
                                    feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStudentGroupAndStatus(studentAdmission2.getStudentRegister().getBranch().getId(), studentAdmission2.getStandard().getId(), studentAdmission2.getAcademicYear().getId(), studentAdmission2.getStudentType(), studentAdmission2.getStudentGroup(), true);
                                else
                                    feesMaster = feesMasterRepository.findByBranchIdAndStandardIdAndAcademicYearIdAndStudentTypeAndStatus(studentAdmission2.getStudentRegister().getBranch().getId(), studentAdmission2.getStandard().getId(), studentAdmission2.getAcademicYear().getId(), studentAdmission2.getStudentType(), true);
                                if (feesMaster != null) {

                                    double outstanding = 0.0;
                                    FeesTransactionSummary feesTransactionSummary1 = feesTransactionSummaryRepository.
                                            findByStudentRegisterIdAndAcademicYearIdAndStatus(studentRegisterId,
                                                    studentAdmission2.getAcademicYear().getId(), true);
                                    if (feesTransactionSummary1 != null) {
                                        oldFeesMaster = feesTransactionSummary1.getFeesMaster();
                                        if (studentAdmission2.getStudentType() == 2 && (studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("11")
                                                || studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                            if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("male")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForBoy());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForBoy());
//                                                if (feesTransactionSummary1.getPaidAmount() > 0) {
//                                                    outstanding = feesMaster.getAmountForBoy() - feesTransactionSummary1.getPaidAmount();
//                                                }
                                                outstanding = feesMaster.getAmountForBoy();
                                            } else if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("female")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForGirl());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForGirl());
//                                                if (feesTransactionSummary1.getPaidAmount() > 0) {
//                                                    outstanding = feesMaster.getAmountForGirl() - feesTransactionSummary1.getPaidAmount();
//                                                } else {
//                                                }
                                                outstanding = feesMaster.getAmountForGirl();
                                            }
                                        } else {
                                            //feesTransactionSummary1.setBalance(feesMaster.getAmount());
                                            feesTransactionSummary1.setTotalFees(0.0);
                                            feesTransactionSummary1.setTotalFees(feesMaster.getAmount());
//                                            if (feesTransactionSummary1.getPaidAmount() > 0 && feesTransactionSummary1.getConcessionAmount() > 0) {
//
//                                                outstanding = feesMaster.getAmount() - feesTransactionSummary1.getPaidAmount();
//                                                outstanding = outstanding - feesTransactionSummary1.getConcessionAmount();
//                                            } else if (feesTransactionSummary1.getPaidAmount() > 0 && feesTransactionSummary1.getConcessionAmount() == 0) {
//                                                outstanding = feesMaster.getAmount() - feesTransactionSummary1.getPaidAmount();
//                                            } else {
//                                            }
                                            outstanding = feesMaster.getAmount();
                                        }
                                        System.out.println("outstanding >>>>>>>>>>>>>>" + outstanding);
                                        double headFee = getBusHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("BUS headFee >>>>>>>>>>>>>>" + headFee);
                                        outstanding = outstanding - headFee; // 71000 - 12000 = 59000
                                        System.out.println("outstanding >>>>>>>>>>>>>>" + outstanding);
                                        Double busfees = 0.0;
                                        StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                                                studentRegisterId, studentAdmission2.getAcademicYear().getId(), true);
                                        if (studentTransport != null) {
                                            busfees = studentTransport.getBus().getBusFee();
                                            outstanding = outstanding + busfees;
                                        }

                                        double vacationHeadFee = getVacationHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("vacationHeadFee >>>>>>>>>>>>>>" + vacationHeadFee);
                                        if (!studentAdmission2.getIsVacation()) { // student vacation not applicable
                                            outstanding = outstanding - vacationHeadFee; // 71000 - 12000 = 59000
                                        }
                                        double scholarshipfee = getScholarshipFee(studentAdmission2.getStudentRegister(), feesMaster); //5500
                                        if (studentAdmission2.getIsScholarship() != null && !studentAdmission2.getIsScholarship()) {
                                            outstanding = outstanding - scholarshipfee;
                                        }
                                        double foundationfee = getFoundationFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsFoundation() != null && !studentAdmission2.getIsFoundation()) {
                                            outstanding = outstanding - foundationfee;
                                        }

                                        double ntsFee = getNtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getNts() != null && !studentAdmission2.getNts()) {
                                            outstanding = outstanding - ntsFee;
                                        }

                                        double mtsFee = getMtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsMts() != null && !studentAdmission2.getIsMts()) {
                                            outstanding = outstanding - mtsFee;
                                        }
                                        double totalFees = outstanding;

                                        if (feesTransactionSummary1.getPaidAmount() > 0 && feesTransactionSummary1.getConcessionAmount() > 0) {
                                            outstanding = outstanding - feesTransactionSummary1.getPaidAmount();
                                            outstanding = outstanding - feesTransactionSummary1.getConcessionAmount();
                                            feesTransactionSummary1.setTotalFees(totalFees);
                                            feesTransactionSummary1.setBalance(outstanding);
                                        } else if (feesTransactionSummary1.getPaidAmount() > 0 && feesTransactionSummary1.getConcessionAmount() == 0) {
                                            outstanding = outstanding - feesTransactionSummary1.getPaidAmount();
                                            feesTransactionSummary1.setTotalFees(totalFees);
                                            feesTransactionSummary1.setBalance(outstanding);
                                        } else {
                                            feesTransactionSummary1.setTotalFees(totalFees);
                                            feesTransactionSummary1.setBalance(outstanding);
                                        }
//                                        feesTransactionSummary1.setPaidAmount(0.0);
                                        feesTransactionSummary1.setStudentRegister(studentAdmission2.getStudentRegister());
                                        feesTransactionSummary1.setStandard(studentAdmission2.getStandard());
                                        feesTransactionSummary1.setDivision(studentAdmission2.getDivision());
                                        feesTransactionSummary1.setFeesMaster(feesMaster);
                                        feesTransactionSummary1.setAcademicYear(studentAdmission2.getAcademicYear());
                                        feesTransactionSummary1.setStudentType(studentAdmission2.getStudentType());
                                        feesTransactionSummary1.setBranch(studentAdmission2.getBranch());
                                        feesTransactionSummary1.setOutlet(studentAdmission2.getOutlet());
                                        feesTransactionSummary1.setStudentGroup(studentAdmission2.getStudentGroup());
                                        feesTransactionSummary1.setCreatedBy(users.getId());
                                        feesTransactionSummary1.setStatus(true);

                                        try {
                                            feesTransactionSummaryRepository.save(feesTransactionSummary1);
                                            System.out.println("Student Feestransaction Saved");
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                            responseMessage.setMessage("Failed to save FeeTranscation");
                                        }
//                                        StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndStatus(studentRegisterId, true);
//                                        if (studentTransport != null) {
//                                            System.out.println("Student transport exists.........");
//                                            studentRegisterRepository.deleteStudentBusId(studentTransport.getId());
//                                        } else {
//                                            System.out.println("Student bus not exists.........");
//                                        }

                                        LedgerMaster ledgerMaster = ledgerMasterRepository.findByStudentRegisterIdAndOutletIdAndBranchIdAndStatus(
                                                studentRegisterId, studentAdmission2.getOutlet().getId(), studentAdmission2.getBranch().getId(), true);

                                        FiscalYear fiscalYear = getFiscalYearFromAcademicYear(studentAdmission2.getAcademicYear().getYear());
//                                        double hostelFee = 0;
                                        if (ledgerMaster != null) {
                                            /******** Get Hostel fee by searching hostel name ********/
//                                            double hostelFee = getHostelHeadFee(studentAdmission2.getStudentRegister(), feesMaster);

                                            /******** Get Hostel fee by get hostel receipt under heads amount ********/
                                            double hostelFee = getHostelHeadFees(studentAdmission2.getStudentRegister(), feesMaster);
                                            System.out.println("hostelFee........." + hostelFee);
                                            double totalFeeExceptHostel = 0.0;
//                                            if(feesTransactionSummary1.getPaidAmount()>0)
//                                            {
                                            totalFeeExceptHostel = totalFees - hostelFee;
//                                            }else {
//                                                 totalFeeExceptHostel = outstanding - hostelFee;
//                                            }
                                            System.out.println("totalFeeExceptHostel........." + totalFeeExceptHostel);
                                            Double sumCR = 0.0;
                                            Double sumDR = 0.0;
                                            Double outstandingBal = 0.0;
                                            sumCR = ledgerTransactionPostingsRepository.findsumCR(ledgerMaster.getId());//-0.20 //0
                                            System.out.println("SumCR:" + sumCR);
                                            sumDR = ledgerTransactionPostingsRepository.findsumDR(ledgerMaster.getId());//-0.40
                                            System.out.println("SumDR:" + sumDR);
                                            TranxSalesInvoice tranxSalesInvoice = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(
                                                    ledgerMaster.getId(), fiscalYear.getId(), true);
                                            if (tranxSalesInvoice != null) {
                                                if (sumCR > 0) {
//                                                    outstandingBal = (totalFeeExceptHostel - sumCR) + totalFees - sumCR;//0-(-0.40)-0.20  0+41500)+0=41500
                                                    outstandingBal = totalFeeExceptHostel - sumCR;

                                                } else {
                                                    outstandingBal = totalFeeExceptHostel;
                                                }
                                                tranxSalesInvoice.setBillDate(studentAdmission2.getDateOfAdmission() != null ? studentAdmission2.getDateOfAdmission() : tranxSalesInvoice.getBillDate());
                                                tranxSalesInvoice.setTotalAmount(totalFeeExceptHostel);
                                                tranxSalesInvoice.setTotalBaseAmount(totalFeeExceptHostel);
                                                tranxSalesInvoice.setBalance(totalFeeExceptHostel);
                                                tranxSalesInvoice.setUpdatedAt(LocalDateTime.now());
                                                tranxSalesInvoice.setUpdatedBy(users.getId());
                                                tranxSalesInvoice.setStatus(true);
                                                tranxSalesInvoiceRepository.save(tranxSalesInvoice);
                                                System.out.println("tranxSalesInvoice.getId() " + tranxSalesInvoice.getId());
                                                tranxSalesInvoiceDetailsRepository.MakeStatusZero(tranxSalesInvoice.getId(), false);
                                            }

                                            try {
                                                /**** New Postings Logic for Sundry Debtors *****/
                                                LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.
                                                        findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                                ledgerMaster.getId(), 17L, tranxSalesInvoice.getId(), true);
                                                if (mLedger != null) {
                                                    mLedger.setAmount(totalFeeExceptHostel);
                                                    mLedger.setTransactionDate(tranxSalesInvoice.getBillDate());
                                                    mLedger.setOperations("Updated");
                                                    ledgerTransactionPostingsRepository.save(mLedger);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                studentLogger.error("Exception => while saving ledgerPostingEdit in student promote" + e);
                                            }

                                            List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(oldFeesMaster.getId(), true);

                                            for (FeesDetails feesDetails : feesDetailsList) {
                                                System.out.println("feesDetails.getFeeHead().getLedger().getId()->" + feesDetails.getFeeHead().getLedger().getId());
                                                /**** New Postings Logic : Edit*****/
                                                LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                        feesDetails.getFeeHead().getLedger().getId(), 17L, tranxSalesInvoice.getId(), true);
                                                if (mLedger != null) {
                                                    mLedger.setAmount(0.0);
                                                    mLedger.setTransactionDate(tranxSalesInvoice.getBillDate());
                                                    mLedger.setOperations("Updated");
                                                    mLedger.setStatus(false);
                                                    ledgerTransactionPostingsRepository.save(mLedger);
                                                } else {
                                                    System.out.println("Fee head ledger not Exist");
                                                }
                                            }

                                            TransactionTypeMaster tranxType = transactionTypeMasterRepository.findByTransactionCodeIgnoreCase("feestr");
                                            insertIntoTranxDetailFA(studentAdmission2.getStudentRegister(), tranxSalesInvoice, tranxType, feesMaster, "create"); // for Fees Accounts : cr


                                            Branch hostelBranch = branchRepository.getHostelBranchRecord(users.getOutlet().getId(), true);
                                            LedgerMaster hostelStudentLedger = null;
                                            if (hostelBranch != null) {
                                                hostelStudentLedger = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(studentRegisterId, hostelBranch.getId(), true);
                                                if (hostelStudentLedger != null) {
                                                    System.out.println("Already student ledger exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                    TranxSalesInvoice salesInvoiceHostel = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(
                                                            hostelStudentLedger.getId(), fiscalYear.getId(), true);

                                                    if (salesInvoiceHostel != null) {
                                                        System.out.println("Already salesInvoiceHostel exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                        salesInvoiceHostel.setTotalAmount(0.0);
                                                        salesInvoiceHostel.setTotalBaseAmount(0.0);
                                                        salesInvoiceHostel.setBalance(0.0);
                                                        salesInvoiceHostel.setUpdatedAt(LocalDateTime.now());
                                                        salesInvoiceHostel.setUpdatedBy(users.getId());
                                                        salesInvoiceHostel.setStatus(false);
                                                        tranxSalesInvoiceRepository.save(salesInvoiceHostel);

                                                        System.out.println("tranxSalesInvoice.getId() " + salesInvoiceHostel.getId());
                                                        tranxSalesInvoiceDetailsRepository.MakeStatusZero(salesInvoiceHostel.getId(), false);
                                                    }
                                                    if (hostelStudentLedger != null) {

                                                        try {
                                                            /**** New Postings Logic : Edit*****/
                                                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                                    hostelStudentLedger.getId(), 17L, salesInvoiceHostel.getId(), true);
                                                            if (mLedger != null) {
                                                                mLedger.setAmount(0.0);
                                                                mLedger.setTransactionDate(salesInvoiceHostel.getBillDate());
                                                                mLedger.setOperations("Updated");
                                                                mLedger.setStatus(false);
                                                                ledgerTransactionPostingsRepository.save(mLedger);
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                                                        }
//
                                                    } else {
                                                        System.out.println("student hostel not found");
                                                    }

                                                    /******** Get Hostel fee by searching hostel name ********/

//
                                                    /******** Get Hostel fee by get hostel receipt under heads amount ********/
                                                    List<FeesDetails> hostelfeesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatusAndHostelsOnly(oldFeesMaster.getId(), false, true);
                                                    for (FeesDetails feesDetails : hostelfeesDetailsList) {
                                                        double fees = 0;
                                                        System.out.println("feesDetails.getFeeHead().getLedger().getId()->" + feesDetails.getFeeHead().getLedger().getId());

                                                        try {

                                                            /**** New Postings Logic : Edit*****/
                                                            LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                                                    feesDetails.getFeeHead().getLedger().getId(), 17L, salesInvoiceHostel.getId(), true);
                                                            if (mLedger != null) {
                                                                mLedger.setAmount(0.0);
                                                                mLedger.setTransactionDate(tranxSalesInvoice.getBillDate());
                                                                mLedger.setOperations("Updated");
                                                                mLedger.setStatus(false);
                                                                ledgerTransactionPostingsRepository.save(mLedger);
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                                                        }

                                                    }

                                                }

                                                System.out.println("hostelFee =>" + hostelFee);
                                                if (hostelFee > 0) {
                                                    if (hostelStudentLedger == null) {
                                                        try {
                                                            System.out.println("hostelStudentLedger created if not exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                            hostelStudentLedger = createLedgerForStudentForHostel(hostelBranch, studentAdmission2.getStudentRegister(), users);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            studentLogger.error("Exception => while hostelStudentLedger " + e);
                                                        }
                                                    }

                                                    try {
                                                        Double sumCR1 = 0.0;
                                                        Double sumDR1 = 0.0;
                                                        Double outstandingBal1 = 0.0;
                                                        sumCR1 = ledgerTransactionPostingsRepository.findsumCR(hostelStudentLedger.getId());//-0.20 //0
                                                        sumDR1 = ledgerTransactionPostingsRepository.findsumDR(hostelStudentLedger.getId());//-0.40
                                                        //outstandingBal1 = sumDR1 - sumCR1;//0-(-0.40)-0.20  0+41500)+0=41500
                                                        outstandingBal1 = (sumDR1 - sumCR1) + sumCR1;
                                                        createTranxSalesInvoiceForHostel(hostelBranch, studentAdmission2.getStudentRegister(),
                                                                users, hostelStudentLedger.getId(), hostelFee, feesMaster, studentAdmission1, 0.0);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        studentLogger.error("Exception => while createTranxSalesInvoiceForHostel" + e);
                                                    }
                                                    System.out.println("salesInvoiceHostel created again >>>>>>>>>>>>>>>>>>>>>>>>>>>");

                                                } else {

                                                    System.out.println("Hostel is not allocated to Student");
                                                }
                                            }
                                        } else {
                                            System.out.println("Ledger Not Found By Student Id in Ledger Master");
                                            responseMessage.setMessage("Ledger Not Found By Student Id in Ledger Master");
                                            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                                        }
                                    } else {
                                        System.out.println("Fees Transaction summary new created");
                                        studentLogger.error("Fees Transaction summary new created fro the student =" + studentAdmission2.getStudentRegister().getId());
                                        feesTransactionSummary1 = new FeesTransactionSummary();
                                        if (studentAdmission2.getStudentType() == 2 && (studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("11")
                                                || studentAdmission2.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                            if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("male")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForBoy());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForBoy());

                                                outstanding = feesMaster.getAmountForBoy();
                                            } else if (studentAdmission2.getStudentRegister().getGender().equalsIgnoreCase("female")) {
                                                feesTransactionSummary1.setBalance(feesMaster.getAmountForGirl());
                                                feesTransactionSummary1.setTotalFees(feesMaster.getAmountForGirl());

                                                outstanding = feesMaster.getAmountForGirl();
                                            }
                                        } else {
                                            feesTransactionSummary1.setTotalFees(0.0);
                                            feesTransactionSummary1.setTotalFees(feesMaster.getAmount());

                                            outstanding = feesMaster.getAmount();
                                        }
                                        System.out.println("outstanding >>>>>>>>>>>>>>" + outstanding);
                                        double headFee = getBusHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("BUS headFee >>>>>>>>>>>>>>" + headFee);
                                        outstanding = outstanding - headFee; // 71000 - 12000 = 59000
                                        System.out.println("outstanding >>>>>>>>>>>>>>" + outstanding);
                                        Double busfees = 0.0;
                                        StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                                                studentRegisterId, studentAdmission2.getAcademicYear().getId(), true);
                                        if (studentTransport != null) {
                                            busfees = studentTransport.getBus().getBusFee();
                                            outstanding = outstanding + busfees;
                                        }

                                        double vacationHeadFee = getVacationHeadFee(studentAdmission2.getStudentRegister(), feesMaster); // 12000
                                        System.out.println("vacationHeadFee >>>>>>>>>>>>>>" + vacationHeadFee);
                                        if (!studentAdmission2.getIsVacation()) { // student vacation not applicable
                                            outstanding = outstanding - vacationHeadFee; // 71000 - 12000 = 59000
                                        }
                                        double scholarshipfee = getScholarshipFee(studentAdmission2.getStudentRegister(), feesMaster); //5500
                                        if (studentAdmission2.getIsScholarship() != null && !studentAdmission2.getIsScholarship()) {
                                            outstanding = outstanding - scholarshipfee;
                                        }
                                        double foundationfee = getFoundationFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsFoundation() != null && !studentAdmission2.getIsFoundation()) {
                                            outstanding = outstanding - foundationfee;
                                        }

                                        double ntsFee = getNtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getNts() != null && !studentAdmission2.getNts()) {
                                            outstanding = outstanding - ntsFee;
                                        }

                                        double mtsFee = getMtsFee(studentAdmission2.getStudentRegister(), feesMaster);
                                        if (studentAdmission2.getIsMts() != null && !studentAdmission2.getIsMts()) {
                                            outstanding = outstanding - mtsFee;
                                        }
                                        double totalFees = outstanding;
                                        feesTransactionSummary1.setPaidAmount(0.0);
                                        feesTransactionSummary1.setTotalFees(totalFees);
                                        feesTransactionSummary1.setBalance(outstanding);
                                        feesTransactionSummary1.setStudentRegister(studentAdmission2.getStudentRegister());
                                        feesTransactionSummary1.setStandard(studentAdmission2.getStandard());
                                        feesTransactionSummary1.setDivision(studentAdmission2.getDivision());
                                        feesTransactionSummary1.setFeesMaster(feesMaster);
                                        feesTransactionSummary1.setAcademicYear(studentAdmission2.getAcademicYear());
                                        feesTransactionSummary1.setStudentType(studentAdmission2.getStudentType());
                                        feesTransactionSummary1.setBranch(studentAdmission2.getBranch());
                                        feesTransactionSummary1.setOutlet(studentAdmission2.getOutlet());
                                        feesTransactionSummary1.setStudentGroup(studentAdmission2.getStudentGroup());
                                        feesTransactionSummary1.setCreatedBy(users.getId());
                                        feesTransactionSummary1.setStatus(true);

                                        try {
                                            feesTransactionSummaryRepository.save(feesTransactionSummary1);
                                            System.out.println("Student Feestransaction Saved");
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                            responseMessage.setMessage("Failed to save FeeTranscation");
                                        }

                                        FiscalYear fiscalYear = getFiscalYearFromAcademicYear(studentAdmission2.getAcademicYear().getYear());

                                        double hostelFee = getHostelHeadFees(studentAdmission2.getStudentRegister(), feesMaster);
                                        double totalFeeExceptHostel = outstanding - hostelFee;
                                        LedgerMaster ledgerMaster = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(
                                                studentRegisterId, users.getBranch().getId(), true);
                                        createTranxSalesInvoice(studentAdmission2.getStudentRegister(), users, ledgerMaster.getId(),
                                                outstanding, feesMaster, totalFeeExceptHostel, studentAdmission2);
                                        if (hostelFee > 0) {
                                            LedgerMaster hostelStudentLedger = null;
                                            Branch hostelBranch = branchRepository.getHostelBranchRecord(users.getOutlet().getId(), true);
                                            if (hostelBranch != null) {
                                                hostelStudentLedger = ledgerMasterRepository.
                                                        findByStudentRegisterIdAndOutletIdAndBranchIdAndStatus(
                                                                studentRegisterId, users.getOutlet().getId(), hostelBranch.getId(), true);
                                                if (hostelStudentLedger != null) {
                                                    createTranxSalesInvoiceForHostel(hostelBranch, studentAdmission2.getStudentRegister(),
                                                            users, hostelStudentLedger.getId(), hostelFee, feesMaster, studentAdmission2, 0.0);
                                                } else {
                                                    try {
                                                        System.out.println("hostelStudentLedger created if not exists >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                        hostelStudentLedger = createLedgerForStudentForHostel(hostelBranch,
                                                                studentAdmission2.getStudentRegister(), users);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        studentLogger.error("Exception => while hostelStudentLedger " + e);
                                                    }
                                                    try {
                                                        createTranxSalesInvoiceForHostel(hostelBranch,  studentAdmission2.getStudentRegister(),
                                                                users, hostelStudentLedger.getId(), hostelFee, feesMaster,
                                                                studentAdmission2, 0.0);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        studentLogger.error("Exception => while createTranxSalesInvoiceForHostel" + e);
                                                    }
                                                    System.out.println("salesInvoiceHostel created again >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                }


                                            } else {
                                                System.out.println("hostel branch not found");
                                            }


                                        } else {
                                            System.out.println("hostel is not allocated to student");
                                        }
                                    }

                                }
                            }
                            responseMessage.setMessage("Student promoted Successfully !");
                            responseMessage.setResponseStatus(HttpStatus.OK.value());
                        } else {
                            responseMessage.setMessage("Student admission record not found");
                            responseMessage.setResponseStatus(HttpStatus.NOT_FOUND.value());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    studentLogger.error("Exception  promoteStudent =>" + e);

                    responseMessage.setMessage("Failed to update student admission");
                    responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                return responseMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            studentLogger.error("Exception promoteStudent =>" + e);

            responseMessage.setMessage("Failed to update student admission");
            responseMessage.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }


    public JsonObject updateInvoiceDates(HttpServletRequest request) {
        JsonObject response = new JsonObject();

        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        List<TranxSalesInvoice> salesInvoices = tranxSalesInvoiceRepository.findByOutletIdAndStatus(users.getOutlet().getId(), true);
//        List<TranxSalesInvoice> salesInvoices = tranxSalesInvoiceRepository.findByOutletIdAndStatusAndId(users.getOutlet().getId(), true, 160L);

        System.out.println("tranxSalesInvoice size-" + salesInvoices.size());
        for (TranxSalesInvoice tranxSalesInvoice : salesInvoices) {
            System.out.println("tranxSalesInvoice ID :" + tranxSalesInvoice.getId());
            System.out.println("getSundryDebtors ID :" + tranxSalesInvoice.getSundryDebtors().getId());
            if (tranxSalesInvoice.getSundryDebtors().getStudentRegister() != null) {
                StudentAdmission studentAdmission = (StudentAdmission) studentAdmissionRepository.findByStudentRegisterIdAndStatus(tranxSalesInvoice.getSundryDebtors().getStudentRegister().getId(), true);

                LocalDate doa = null;
                if (studentAdmission != null) {
                    System.out.println("studentAdmission ID :" + studentAdmission.getId());
                    if (studentAdmission.getDateOfAdmission() != null) {
                        doa = studentAdmission.getDateOfAdmission();
                    } else if (studentAdmission.getStudentRegister().getDateOfAdmission() != null) {
                        doa = studentAdmission.getStudentRegister().getDateOfAdmission();
                        studentAdmission.setDateOfAdmission(studentAdmission.getStudentRegister().getDateOfAdmission());
                        studentAdmission.setIsRightOff(false);
                        studentAdmissionRepository.save(studentAdmission);
                    }

                    if (doa == null) doa = tranxSalesInvoice.getBillDate();

                    try {

                        ledgerTransactionDetailsRepository.updateInvoiceTransactionDate(doa, tranxSalesInvoice.getId(), 17L);

                        tranxSalesInvoice.setBillDate(doa);
                        tranxSalesInvoiceRepository.save(tranxSalesInvoice);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Exception " + e.getMessage());
                    }
                }
            }
        }

        return response;
    }


    public JsonObject rollbackInvoiceDates(HttpServletRequest request) {
        JsonObject response = new JsonObject();

        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        List<TranxSalesInvoice> salesInvoices = tranxSalesInvoiceRepository.findByOutletIdAndStatus(users.getOutlet().getId(), true);
        System.out.println("tranxSalesInvoice size-" + salesInvoices.size());
        for (TranxSalesInvoice tranxSalesInvoice : salesInvoices) {
            System.out.println("tranxSalesInvoice ID :" + tranxSalesInvoice.getId());
            System.out.println("getSundryDebtors ID :" + tranxSalesInvoice.getSundryDebtors().getId());
            if (tranxSalesInvoice.getSundryDebtors().getStudentRegister() != null) {

                try {
//                    LocalDate invoiceDate = tranxSalesInvoice.getCreatedAt().toLocalDate();
                    LocalDate invoiceDate = LocalDate.parse("2022-04-01");
                    ledgerTransactionDetailsRepository.updateInvoiceTransactionDate(invoiceDate, tranxSalesInvoice.getId(), 17L);
                    tranxSalesInvoice.setBillDate(invoiceDate);
                    tranxSalesInvoiceRepository.save(tranxSalesInvoice);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception " + e.getMessage());
                }
            }
        }

        return response;
    }

    public Object deletePromotion(HttpServletRequest request) {
        JsonObject responseMessage = new JsonObject();
        Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));

        try {
            Long id = Long.valueOf(request.getParameter("studentId"));
            Long studentAdmissionId = Long.valueOf(request.getParameter("studentAdmissionId"));
            String operationType = request.getParameter("operationType");
            System.out.println("student id =>" + id);
            StudentRegHistory studentRegHistory = null;
            StudentAdmission studentAdmission = null;

            StudentRegister studentRegister = studentRegisterRepository.findById(id).get();
            if (studentRegister != null) {
                studentAdmission = studentAdmissionRepository.findByIdAndStatus(studentAdmissionId, true);
                if (studentAdmission != null) {
                    StudentAdmHistory studentAdmHistory = saveAdmHistory(studentAdmission, users, operationType);
                    FeesTransactionSummary feesTransactionSummary = feesTransactionSummaryRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                            studentRegister.getId(), studentAdmission.getAcademicYear().getId(), true);
                    if (feesTransactionSummary != null) {
                        if (feesTransactionSummary.getPaidAmount() == 0) {
                            FeesTranxSummaryHistory feesTranxSummaryHistory = saveFeesTranxsummaryHistroy(feesTransactionSummary, users, operationType);
                            feesTransactionSummaryRepository.deleteStudentFeesSummaryByStudentIdAndAcademicId(studentRegister.getId(), studentAdmission.getAcademicYear().getId());
                        } else {
                            responseMessage.addProperty("message", "First Delete their Respective Transaction/Payments");
                            responseMessage.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
                        }
                    } else {
                        System.out.println("Student not found in Fees tranx Summary, please connect to admin");

                    }
                    StudentTransport studentTransport = studentTransportRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                            studentRegister.getId(), studentAdmission.getAcademicYear().getId(), true);
                    if (studentTransport != null) {
                        StudentTransportHistory studentTransportHistory = saveInStudentTransportHistory(studentTransport, users, operationType);
                        studentTransportRepository.deleteTransportRecord(id, studentAdmission.getAcademicYear().getId());
                    } else {
                        System.out.println("Student not found in student Transport");
                    }

                    FiscalYear fiscalYear = getFiscalYearFromAcademicYear(studentAdmission.getAcademicYear().getYear());

                    LedgerMaster ledgerMaster = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(
                            studentRegister.getId(), users.getBranch().getId(), true);
                    if (ledgerMaster != null) {
                        TranxSalesInvoice tranxSalesInvoice = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(
                                ledgerMaster.getId(), fiscalYear.getId(), true);
                        if (tranxSalesInvoice != null) {
                            SalesInvoiceHistory salesInvoiceHistory = saveSaleInvoicehistory(tranxSalesInvoice, users, operationType);

                            List<TranxSalesInvoiceDetails> tranxSalesInvoiceDetails = tranxSalesInvoiceDetailsRepository.findByTranxSalesInvoiceIdAndStatus(tranxSalesInvoice.getId(), true);
                            if (tranxSalesInvoiceDetails.size() > 0) {
                                for (TranxSalesInvoiceDetails tranxSalesInvoiceDetails1 : tranxSalesInvoiceDetails) {

                                    System.out.println("TRANX DET ID " + tranxSalesInvoiceDetails1.getId() + "LDID " + tranxSalesInvoiceDetails1.getFeeHead().getLedger().getId() + " TRID " + tranxSalesInvoice.getId() + " trdate " + tranxSalesInvoice.getBillDate() + " paidAmt " + tranxSalesInvoiceDetails1.getAmount() + " CR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                    SalesInvoiceDetailsHistory salesInvoiceDetailsHistory = new SalesInvoiceDetailsHistory();
                                    salesInvoiceDetailsHistory.setAmount(tranxSalesInvoiceDetails1.getAmount());
                                    salesInvoiceDetailsHistory.setTranxSalesInvoiceDetailsId(tranxSalesInvoiceDetails1.getId());
                                    salesInvoiceDetailsHistory.setBranchId(tranxSalesInvoiceDetails1.getBranch().getId());
                                    salesInvoiceDetailsHistory.setOutletId(tranxSalesInvoiceDetails1.getOutlet().getId());
                                    salesInvoiceDetailsHistory.setOperationType(operationType);
                                    salesInvoiceDetailsHistory.setCreatedBy(users.getId());
                                    if (tranxSalesInvoiceDetails1.getFiscalYear() != null) {
                                        salesInvoiceDetailsHistory.setFiscalYearId(tranxSalesInvoiceDetails1.getFiscalYear().getId());
                                    }
                                    salesInvoiceDetailsHistory.setStatus(true);
                                    if (tranxSalesInvoiceDetails1.getFeeHead() != null) {
                                        salesInvoiceDetailsHistory.setFee_head_id(tranxSalesInvoiceDetails1.getFeeHead().getId());
                                    }
                                    salesInvoiceDetailsHistoryRepository.save(salesInvoiceDetailsHistory);
                                }
                                studentRegisterRepository.deleteSalesTranxDetailsBySalesInvoiceId(tranxSalesInvoice.getId());
                            } else {
                                System.out.println("sales invoice Details not Found");
                            }
                            ledgerTransactionPostingsRepository.updateToStatusZeroTranxLedgerDetailByTranxIdAndTranxType(
                                    tranxSalesInvoice.getId(), 17);
                            tranxSalesInvoiceRepository.deleteSalesTransDataById(tranxSalesInvoice.getId());
                        }
                        ledgerTransactionPostingsRepository.updateToStatusZeroTranxLedgerDetailByLedgerIdAndTranxType(
                                ledgerMaster.getId(), fiscalYear.getId(), 17);
                    } else {
                        System.out.println("ledger not found");
                    }


                    Branch hostelBranch = branchRepository.getHostelBranchRecord(users.getOutlet().getId(), true);
                    if (hostelBranch != null) {
                        LedgerMaster ledgerMasterHostel = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(
                                studentRegister.getId(), hostelBranch.getId(), true);
                        if (ledgerMasterHostel != null) {
                            TranxSalesInvoice tranxSalesInvoice = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(
                                    ledgerMasterHostel.getId(), fiscalYear.getId(), true);
                            if (tranxSalesInvoice != null) {
                                SalesInvoiceHistory salesInvoiceHistory = saveSaleInvoicehistory(tranxSalesInvoice, users, operationType);

                                List<TranxSalesInvoiceDetails> tranxSalesInvoiceDetails = tranxSalesInvoiceDetailsRepository.findByTranxSalesInvoiceIdAndStatus(tranxSalesInvoice.getId(), true);
                                if (tranxSalesInvoiceDetails.size() > 0) {
                                    for (TranxSalesInvoiceDetails tranxSalesInvoiceDetails1 : tranxSalesInvoiceDetails) {

                                        System.out.println("TRANX DET ID " + tranxSalesInvoiceDetails1.getId() + "LDID " + tranxSalesInvoiceDetails1.getFeeHead().getLedger().getId() + " TRID " + tranxSalesInvoice.getId() + " trdate " + tranxSalesInvoice.getBillDate() + " paidAmt " + tranxSalesInvoiceDetails1.getAmount() + " CR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                        SalesInvoiceDetailsHistory salesInvoiceDetailsHistory = new SalesInvoiceDetailsHistory();
                                        salesInvoiceDetailsHistory.setAmount(tranxSalesInvoiceDetails1.getAmount());
                                        salesInvoiceDetailsHistory.setTranxSalesInvoiceDetailsId(tranxSalesInvoiceDetails1.getId());
                                        salesInvoiceDetailsHistory.setBranchId(tranxSalesInvoiceDetails1.getBranch().getId());
                                        salesInvoiceDetailsHistory.setOutletId(tranxSalesInvoiceDetails1.getOutlet().getId());
                                        salesInvoiceDetailsHistory.setOperationType(operationType);
                                        salesInvoiceDetailsHistory.setCreatedBy(users.getId());
                                        if (tranxSalesInvoiceDetails1.getFiscalYear() != null) {
                                            salesInvoiceDetailsHistory.setFiscalYearId(tranxSalesInvoiceDetails1.getFiscalYear().getId());
                                        }
                                        salesInvoiceDetailsHistory.setStatus(true);
                                        if (tranxSalesInvoiceDetails1.getFeeHead() != null) {
                                            salesInvoiceDetailsHistory.setFee_head_id(tranxSalesInvoiceDetails1.getFeeHead().getId());
                                        }
                                        salesInvoiceDetailsHistoryRepository.save(salesInvoiceDetailsHistory);
                                    }
                                    studentRegisterRepository.deleteSalesTranxDetailsBySalesInvoiceId(tranxSalesInvoice.getId());
                                } else {
                                    System.out.println("sales invoice Details not Found");
                                }
                                ledgerTransactionPostingsRepository.updateToStatusZeroTranxLedgerDetailByTranxIdAndTranxType(
                                        tranxSalesInvoice.getId(), 17);
                                tranxSalesInvoiceRepository.deleteSalesTransDataById(tranxSalesInvoice.getId());
                            }
                            ledgerTransactionPostingsRepository.updateToStatusZeroTranxLedgerDetailByLedgerIdAndTranxType(
                                    ledgerMasterHostel.getId(), fiscalYear.getId(), 17);
                        } else {
                            System.out.println("ledger not found");
                        }
                    }
                    studentRegisterRepository.deleteStudentAdmissionFromAdmissionById(studentAdmission.getId());

                    responseMessage.addProperty("message", "Student admission data deleted successfully");
                    responseMessage.addProperty("responseStatus", HttpStatus.OK.value());
                } else {
                    responseMessage.addProperty("message", "Student not found, please connect to admin");
                    responseMessage.addProperty("responseStatus", HttpStatus.NOT_FOUND.value());
                }
            } else {
                responseMessage.addProperty("message", "Student not found, please connect to admin");
                responseMessage.addProperty("responseStatus", HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage.addProperty("message", "Failed to delete student admission");
            responseMessage.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseMessage;
    }

    public FiscalYear getFiscalYearFromAcademicYear(String year) {
        String[] adYear = year.split("-");
        String data = adYear[0] + "-";
//        System.out.println("data"+data);
        FiscalYear fiscalYear = fiscalYearRepository.getFiscalYear(data);
//        System.out.println("fiscalYear" + fiscalYear.getFiscalYear());
        return fiscalYear;
    }


    public JsonObject getStudentListByStandardBkp(HttpServletRequest request) {
        JsonObject result = new JsonObject();
        JsonArray res = new JsonArray();
        Map<String, String[]> paramMap = request.getParameterMap();
        Long standardId = 0L;
        Integer studentType = 0;
        Long academicYearId = 0L;
        Long busStopId = 0L;
        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));

            Long branchId = users.getBranch().getId();
            if (paramMap.containsKey("standardId")) standardId = Long.valueOf(request.getParameter("standardId"));
            if (paramMap.containsKey("busStopId")) busStopId = Long.valueOf(request.getParameter("busStopId"));
            if (paramMap.containsKey("studentType")) studentType = Integer.valueOf(request.getParameter("studentType"));
            if (paramMap.containsKey("academicYearId"))
                academicYearId = Long.valueOf(request.getParameter("academicYearId"));
            Long id = users.getOutlet().getId();

            List<Object[]> list = null;
            if (academicYearId != 0 && standardId != 0 && studentType != 0 && busStopId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdAndStandardIdANDStudentTypeAndBusStopIdAndStatus(id, branchId, academicYearId, standardId, studentType, busStopId, true);
            } else if (academicYearId != 0 && standardId != 0 && studentType != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdAndStandardIdANDStudentTypeAndStatus(id, branchId, academicYearId, standardId, studentType, true);
            } else if (academicYearId != 0 && standardId != 0 && busStopId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdAndStandardIdANDBusStopIdAndStatus(id, branchId, academicYearId, standardId, busStopId, true);
            } else if (academicYearId != 0 && standardId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdStandardIdANDStatus(id, branchId, academicYearId, standardId, true);
            } else if (academicYearId != 0 && studentType != 0 && busStopId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdStudentTypeANDBusStopdIdANdStatus(id, branchId, academicYearId, studentType, busStopId, true);
            } else if (academicYearId != 0 && studentType != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdStudentTypeANDStatus(id, branchId, academicYearId, studentType, true);
            } else if (academicYearId != 0 && busStopId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdBusStopIdANDStatus(id, branchId, academicYearId, busStopId, true);
            } else if (standardId != 0 && busStopId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndStandardIdAndBusStopIdANDStatus(id, branchId, standardId, busStopId, true);
            } else if (standardId != 0 && studentType != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndStandardIdAndStudentTypeANDStatus(id, branchId, standardId, studentType, true);
            } else if (academicYearId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndAcademicYearIdANDStatus(id, branchId, academicYearId, true);
            } else if (standardId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndStandardIdANDStatus(id, branchId, standardId, true);
            } else if (studentType != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndStudentTypeANDStatus(id, branchId, studentType, true);
            } else if (busStopId != 0) {
                list = studentRegisterRepository.getStudentsByOutletIdAndBranchIdAndBusStopIdANDStatus(id, branchId, busStopId, true);
            } else {
                System.out.println("last else executing");
//                list = studentRegisterRepository.getDataByStatus(users.getOutlet().getId(), users.getBranch().getId(),academicyearid, true);
                list = studentRegisterRepository.getDataByStatus(users.getOutlet().getId(), users.getBranch().getId(), true);

            }


            System.out.println("list size " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = list.get(i);

                JsonObject response = new JsonObject();
                response.addProperty("TypeofStudent", "");
                response.addProperty("studentType", "");
                response.addProperty("busStopName", "");
                response.addProperty("busStopFee", "");
                response.addProperty("academicYear", "");
                response.addProperty("standardName", "");

                System.out.println("studentId =>" + obj[0].toString());
//                StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(Long.parseLong(obj[0].toString()), true);
                if (standardId == 0 && academicYearId == 0 && studentType == 0) {
                    String studAdmissionData = studentAdmissionRepository.getLastAcademicData(Long.parseLong(obj[0].toString()));
                    System.out.println("studAdmissionData" + studAdmissionData);
//
                    if (obj[10] != null) {
                        if (!obj[10].toString().equalsIgnoreCase("") && Integer.parseInt(obj[10].toString()) == 1) {
                            response.addProperty("TypeofStudent", "New");
                        } else if (!obj[10].toString().equalsIgnoreCase("") && Integer.parseInt(obj[10].toString()) == 2) {
                            response.addProperty("TypeofStudent", "Old");
                        }
                    }

                    if (studAdmissionData != null) {
                        String[] stdData = studAdmissionData.split(",");
                        if (stdData != null) {
                            if (stdData.length > 2 && !stdData[2].equalsIgnoreCase("") && Integer.parseInt(stdData[2]) == 1) {
                                response.addProperty("studentType", "Day Scholar");
                            } else if (stdData.length > 2 && !stdData[2].equalsIgnoreCase("") && Integer.parseInt(stdData[2]) == 2) {
                                response.addProperty("studentType", "Residential");
                            }
                            response.addProperty("academicYear", stdData.length > 0 && stdData[0] != null ? stdData[0] : "");
                            response.addProperty("standardName", stdData.length > 1 && stdData[1] != null ? stdData[1] : "");
                        }
                    }

                    String busData = studentRegisterRepository.getStudentBusData(obj[0].toString(), academicYearId);
                    System.out.println("busData ===>" + busData);
                    if (busData != null) {
                        String[] studBusData = busData.split(",");
                        if (!studBusData[0].equalsIgnoreCase("")) {
                            response.addProperty("busStopName", studBusData[0]);
                        }
                        if (!studBusData[1].equalsIgnoreCase("")) {
                            response.addProperty("busStopFee", studBusData[1]);
                        }
                    }

                    String studentName = obj[2].toString();
                    String fatherName = obj[7].toString() != null ? obj[7].toString() : " ";
                    if (!fatherName.equalsIgnoreCase("")) studentName = studentName + " " + fatherName;
                    studentName = studentName + " " + obj[1].toString();
                    System.out.println("studentName =====>>>>>>>>>>>" + studentName);

                    response.addProperty("fatherName", fatherName);
                    response.addProperty("motherName", obj[8].toString() != null ? obj[8].toString() : "");
                    response.addProperty("address", obj[9].toString() != null ? obj[9].toString() : "");
                    response.addProperty("casteName", obj[11].toString() != null ? obj[11].toString() : "");
                    response.addProperty("subCasteName", obj[12].toString() != null ? obj[12].toString() : "");
                    response.addProperty("category", obj[13].toString() != null ? obj[13].toString() : "");
                    response.addProperty("aadharNo", obj[14].toString() != null ? obj[14].toString() : "");
                    response.addProperty("studentId", obj[15].toString() != null ? obj[15].toString() : "");
                    response.addProperty("studentName", studentName);
                    response.addProperty("studentAdmissionId", obj[16].toString() != null ? obj[16].toString() : "");


                    if (!obj[6].toString().equalsIgnoreCase("")) {
                        response.addProperty("dateOfAdmission", obj[6].toString());
                    } else {
                        if (!obj[17].toString().equalsIgnoreCase("")) {
                            response.addProperty("dateOfAdmission", obj[17].toString());
                        } else {
                            response.addProperty("dateOfAdmission", "");
                        }
                    }

                } else {
                    System.out.println("studentType =>" + obj[7]);
                    if (!obj[7].toString().equalsIgnoreCase("") && Integer.parseInt(obj[7].toString()) == 1) {
                        response.addProperty("studentType", "Day Scholar");
                    } else if (!obj[7].toString().equalsIgnoreCase("") && Integer.parseInt(obj[7].toString()) == 2) {
                        response.addProperty("studentType", "Residential");
                    }
                    if (obj[10] != null) {
                        if (Integer.parseInt(obj[10].toString()) == 1) {
                            response.addProperty("TypeofStudent", "New");
                        } else if (Integer.parseInt(obj[10].toString()) == 2) {
                            response.addProperty("TypeofStudent", "Old");
                        }
                    }

                    String busData = studentRegisterRepository.getStudentBusData(obj[0].toString(), academicYearId);
                    System.out.println("busData ===>" + busData);
                    if (busData != null) {
                        String[] studBusData = busData.split(",");
                        if (!studBusData[0].equalsIgnoreCase("")) {
                            response.addProperty("busStopName", studBusData[0]);
                        }
                        if (!studBusData[1].equalsIgnoreCase("")) {
                            response.addProperty("busStopFee", studBusData[1]);
                        }
                    }

                    String studentName = obj[2].toString();
                    String fatherName = obj[11].toString() != null ? obj[11].toString() : " ";
                    if (!fatherName.equalsIgnoreCase("")) {
                        studentName = studentName + " " + fatherName;
                    }
                    studentName = studentName + " " + obj[1].toString();
                    System.out.println("studentName =====>>>>>>>>>>>" + studentName);

                    response.addProperty("standardName", obj[8].toString());
                    response.addProperty("academicYear", obj[9].toString());
                    response.addProperty("fatherName", fatherName);
                    response.addProperty("motherName", obj[12].toString() != null ? obj[12].toString() : "");
                    response.addProperty("address", obj[13].toString() != null ? obj[13].toString() : "");
                    response.addProperty("casteName", obj[14].toString() != null ? obj[14].toString() : "");
                    response.addProperty("subCasteName", obj[15].toString() != null ? obj[15].toString() : "");
                    response.addProperty("category", obj[16].toString() != null ? obj[16].toString() : "");
                    response.addProperty("aadharNo", obj[17].toString() != null ? obj[17].toString() : "");
                    response.addProperty("studentId", obj[18].toString() != null ? obj[18].toString() : "");
                    System.out.println("Student Admission Id" + obj[19].toString());
                    response.addProperty("studentAdmissionId", obj[19].toString() != null ? obj[19].toString() : "");

                    if (!obj[6].toString().equalsIgnoreCase("")) {
                        response.addProperty("dateOfAdmission", obj[6].toString());
                    } else {
                        if (!obj[20].toString().equalsIgnoreCase("")) {
                            response.addProperty("dateOfAdmission", obj[20].toString());
                        } else {
                            response.addProperty("dateOfAdmission", "");

                        }
                    }
                    response.addProperty("studentName", studentName);
                }

                System.out.println("Student Register Id" + obj[0].toString());
                response.addProperty("id", obj[0].toString());
                response.addProperty("firstName", obj[1].toString());
                response.addProperty("lastName", obj[2].toString());
                response.addProperty("gender", obj[3].toString());
                response.addProperty("mobileNo", obj[4].toString());
                response.addProperty("birthDate", obj[5].toString() != null ? obj[5].toString() : "");
//                response.addProperty("dateOfAdmission", obj[6].toString());

//                response.addProperty("standardName", obj[8].toString());
//                response.addProperty("academicYear", obj[9].toString());


                res.add(response);
            }
            result.add("responseObject", res);
            result.addProperty("responseStatus", HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty(",message", "Failed to load Data");
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;


    }

    public JsonObject getStudentPromotionList(HttpServletRequest request) {
        JsonObject result = new JsonObject();
        JsonArray res = new JsonArray();

        try {
            Users users = jwtRequestFilter.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            Long branchId = users.getBranch().getId();

            String query2 = "SELECT sr.first_name, sr.middle_name, sr.last_name, sr.mobile_no, sr.birth_date, sr.current_address, sta.*," +
                    " str.id AS str_id, str.bus_id, bus_tbl.bus_stop_name, bus_tbl.bus_fee, sr.mother_name,sr.gender, sr.aadhar_no, sr.student_id AS sr_stud_id," +
                    " caste_tbl.caste_name, sub_caste_tbl.sub_caste_name, caste_category_tbl.category_name FROM `student_admission_tbl` AS sta LEFT JOIN" +
                    " student_transport_tbl AS str ON (sta.academic_year_id=str.academic_year_id AND sta.student_id=str.student_id)" +
                    " LEFT JOIN bus_tbl ON str.bus_id=bus_tbl.id LEFT JOIN student_register_tbl AS sr ON sta.student_id=sr.id" +
                    " LEFT JOIN caste_tbl ON sr.caste_id=caste_tbl.id LEFT JOIN sub_caste_tbl ON sr.sub_caste_id=sub_caste_tbl.id" +
                    " LEFT JOIN caste_category_tbl ON sr.caste_category_id=caste_category_tbl.id" +
                    "  WHERE sta.outlet_id=" + users.getOutlet().getId() + " AND sta.branch_id=" + branchId + " AND sta.status=1 ";

            if (!request.getParameter("academicYearId").equals(""))
                query2 += " AND sta.academic_year_id=" + request.getParameter("academicYearId");
            if (!request.getParameter("standardId").equals(""))
                query2 += " AND sta.standard_id=" + request.getParameter("standardId");
            if (!request.getParameter("studentType").equals(""))
                query2 += " AND sta.student_type=" + request.getParameter("studentType");
            if (!request.getParameter("busStopId").equals(""))
                query2 += " AND str.bus_id=" + request.getParameter("busStopId");

            if (request.getParameter("academicYearId").equals("") && request.getParameter("standardId").equals("") &&
                    request.getParameter("studentType").equals("") && request.getParameter("busStopId").equals(""))
                query2 += " ORDER BY sta.id DESC LIMIT 50";
            else
                query2 += " ORDER BY TRIM(sr.last_name)";
            System.out.println("query2 " + query2);
            Query q2 = entityManager.createNativeQuery(query2);
            List<Object[]> list = q2.getResultList();
            System.out.println("list.size() " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = list.get(i);

                JsonObject response = new JsonObject();
                response.addProperty("TypeofStudent", "");
                response.addProperty("studentType", "");
                response.addProperty("busStopName", "");
                response.addProperty("busStopFee", "");
                response.addProperty("academicYear", "");
                response.addProperty("standardName", "");
                response.addProperty("dateOfAdmission", "");

                if (obj[19] != null) {
                    if (!obj[19].toString().equalsIgnoreCase("") && Integer.parseInt(obj[19].toString()) == 1) {
                        response.addProperty("TypeofStudent", "New");
                    } else if (!obj[19].toString().equalsIgnoreCase("") && Integer.parseInt(obj[19].toString()) == 2) {
                        response.addProperty("TypeofStudent", "Old");
                    }
                }
                StudentAdmission studAdmissionData = studentAdmissionRepository.findByIdAndStatus(Long.parseLong(obj[6].toString()), true);
                if (studAdmissionData != null) {
                    if (studAdmissionData.getStudentType() != null && studAdmissionData.getStudentType() == 1) {
                        response.addProperty("studentType", "Day Scholar");
                    } else if (studAdmissionData.getStudentType() != null && studAdmissionData.getStudentType() == 2) {
                        response.addProperty("studentType", "Residential");
                    }
                    response.addProperty("academicYear", studAdmissionData.getAcademicYear().getYear());
                    response.addProperty("standardName", studAdmissionData.getStandard().getStandardName());
                    response.addProperty("dateOfAdmission", studAdmissionData.getDateOfAdmission() != null ?
                            studAdmissionData.getDateOfAdmission().toString() : "");
                }

                response.addProperty("recordCanDelete", false);
                String studAdmId = studentAdmissionRepository.checkForDelete(obj[9].toString());
                if (studAdmId != null && !studAdmId.equals("null") && obj[6].toString().equals(studAdmId))
                    response.addProperty("recordCanDelete", true);

                response.addProperty("busStopName", obj[39] != null ? obj[39].toString() : "");
                response.addProperty("busStopFee", obj[40] != null ? obj[40].toString() : "");

                String studentName = obj[0].toString();
                String fatherName = obj[1] != null ? obj[1].toString() : " ";
                if (!fatherName.equalsIgnoreCase("")) studentName = studentName + " " + fatherName;
                studentName = studentName + " " + obj[2].toString();
                System.out.println("studentName =====>>>>>>>>>>>" + studentName);

                response.addProperty("fatherName", fatherName);
                response.addProperty("motherName", obj[41] != null ? obj[41].toString() : "");
                response.addProperty("address", obj[5] != null ? obj[5].toString() : "");
                response.addProperty("casteName", obj[45] != null ? obj[45].toString() : "");
                response.addProperty("subCasteName", obj[46] != null ? obj[46].toString() : "");
                response.addProperty("category", obj[47] != null ? obj[47].toString() : "");
                response.addProperty("aadharNo", obj[43] != null ? obj[43].toString() : "");
                response.addProperty("studentId", obj[44] != null ? obj[44].toString() : "");
                response.addProperty("studentName", studentName);
                response.addProperty("studentAdmissionId", studAdmissionData != null ?
                        studAdmissionData.getId() : 0);
                System.out.println("Student Register Id" + obj[9].toString());
                response.addProperty("id", obj[9].toString());
                response.addProperty("firstName", obj[0].toString());
                response.addProperty("lastName", obj[2].toString());
                response.addProperty("gender", obj[42] != null ? obj[42].toString() : "");
                response.addProperty("mobileNo", obj[3] != null ? obj[3].toString() : "");
                response.addProperty("birthDate", obj[4] != null ? obj[4].toString() : "");
                res.add(response);
            }
            result.add("responseObject", res);
            result.addProperty("responseStatus", HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty(",message", "Failed to load Data");
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;
    }
}

