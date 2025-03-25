import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.geekapps.geekstack',
  appName: 'geekstack',
  webDir: 'dist/geekstack-app/browser',
  server: {
    allowNavigation: ['api.geekstack.dev'],
    androidScheme: 'https',
    iosScheme: 'https',
  },
  plugins:{
    "FirebaseAuthentication": {
      "skipNativeAuth": false,
      "providers": ["google.com"]
    }
  }
};

export default config;
