file_name = "LangtonRule.txt"

# Open the file and process each line
with open(file_name, "r") as file:
  for line in file:
    number = line.strip()
    if len(number) == 6:
      first_five = number[:5]
      last_digit = number[5]
      print(f'RULES_MAP.put("{first_five}", {last_digit});')