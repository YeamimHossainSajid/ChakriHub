package com.example.ChakriHub.controller;

import com.example.ChakriHub.config.cvparsing.PdfService;
import com.example.ChakriHub.payload.request.CandidateRequestDto;
import com.example.ChakriHub.payload.response.CandidateResponseDto;
import com.example.ChakriHub.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    private PdfService pdfService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @GetMapping
    public ResponseEntity<List<CandidateResponseDto>> getAllCandidates() {
        List<CandidateResponseDto> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }



    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDto> getCandidate(@PathVariable Long id) {
        CandidateResponseDto candidate = candidateService.getCandidate(id);
        return ResponseEntity.ok(candidate);
    }


    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addCandidate(@ModelAttribute CandidateRequestDto candidateRequestDto) throws IOException {
        candidateService.addCandidate(candidateRequestDto);
        return ResponseEntity.ok("Candidate added successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateCandidate(@RequestBody CandidateRequestDto candidateRequestDto, @PathVariable Long id) throws IOException {
        candidateService.updateCandidate(candidateRequestDto, id);
        return ResponseEntity.ok("Candidate updated successfully");
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok("Candidate deleted successfully");
    }

    @PostMapping(value = "/extract", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String extractPdfText(@RequestParam("file") MultipartFile file) {
        try {

            if (!file.getContentType().equals("application/pdf")) {
                return "Please upload a valid PDF file.";
            }


            File tempFile = File.createTempFile("uploaded-", ".pdf");
            file.transferTo(tempFile);


            String extractedText = pdfService.extractTextFromPdf(tempFile.getAbsolutePath());


            tempFile.delete();

            return extractedText;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error extracting text from PDF: " + e.getMessage();
        }
    }

}

